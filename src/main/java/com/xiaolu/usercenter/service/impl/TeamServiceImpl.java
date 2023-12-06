package com.xiaolu.usercenter.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolu.usercenter.common.ErrorCode;
import com.xiaolu.usercenter.common.TeamStatusEnum;
import com.xiaolu.usercenter.exception.BusinessException;
import com.xiaolu.usercenter.model.domain.Team;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.model.domain.UserTeam;
import com.xiaolu.usercenter.model.dto.TeamQuery;
import com.xiaolu.usercenter.model.request.TeamJoinRequest;
import com.xiaolu.usercenter.model.request.TeamQuitRequest;
import com.xiaolu.usercenter.model.request.TeamUpdateRequest;
import com.xiaolu.usercenter.model.vo.TeamUserVO;
import com.xiaolu.usercenter.model.vo.UserVO;
import com.xiaolu.usercenter.service.TeamService;
import com.xiaolu.usercenter.mapper.TeamMapper;
import com.xiaolu.usercenter.service.UserService;
import com.xiaolu.usercenter.service.UserTeamService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* @author 16385
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2023-11-24 10:34:29
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // 1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // 3. 校验信息
        //    1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //    2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //    3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //    4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(-1);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //    5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum) &&
                (StringUtils.isBlank(password) || password.length() > 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
        }
        //    6. 过期时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "过期时间 > 当前时间");
        }
        //    7. 校验用户最多创建 5 个队伍
        // TODO: 2023/11/24 有 bug，并发操作时可能同时创建100多个队伍
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Team::getUserId, userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建 5 个队伍");
        }
        // 4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        // 5. 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, Boolean isAdmin) {
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        // 组合查询条件
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq(Team::getId, id);
            }
            // 根据 id 列表查询
            List<Long> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)) {
                queryWrapper.in(Team::getId, idList);
            }
            // 搜索关键词（同时对队伍名称和描述搜索）
            String searchText = teamQuery.getSearchText();
            // 如果不为空则同时对队伍名称和描述搜索
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like(Team::getName, searchText).or().like(Team::getDescription, searchText));
            }
            String name = teamQuery.getName();
            // 如果不为空则同时对队伍名称搜索
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like(Team::getName, name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like(Team::getDescription, description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq(Team::getMaxNum, maxNum);
            }
            // 根据创建人来查询
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq(Team::getUserId, userId);
            }
            // 根据状态来查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            // 如果不是管理员则只能查看public
            // TODO 貌似自己创建的私密队伍也看不了
            // if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
            //     throw new BusinessException(ErrorCode.NO_AUTH);
            // }

            queryWrapper.eq(Team::getStatus, statusEnum.getValue());

        }
        // 不展示已过期的队伍 (自定义SQL查询)
        // expireTime is null or expireTime > now()
        // LT: less than; GT: greater than NE: not equal
        queryWrapper.and(qw -> qw.gt(Team::getExpireTime, new Date()).or().isNull(Team::getExpireTime));

        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();

            try {
                BeanUtils.copyProperties(teamUserVO, team);
                if (user != null) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(userVO, user);
                    teamUserVO.setCreateUser(userVO);
                }
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }

            teamUserVOList.add(teamUserVO);
        }

        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 只有管理员或者队伍的创建者可以修改
        if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 如果队伍状态为加密，必须要有密码，其余情况不允许有密码
        TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        // TODO: 2023/11/28 这里有小 bug， 如果之前是加密房间就算不传密码也不回报错
        if (enumByValue.equals(TeamStatusEnum.SECRET)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要有密码");
            }
        }

        //todo 如果用户传入的新值和老值一致，就不用 update 了（可自行实现，降低数据库使用次数）
        Team updateTeam = new Team();
        try {
            BeanUtils.copyProperties(updateTeam, teamUpdateRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Bean COPE ERROR");
        }

        return this.updateById(updateTeam);
    }

    @Override
    public Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(teamStatusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
        }
        // 用户不能加入自己的队伍里
        // TODO: 2023/11/28 bug：如果创建人退出了，那么这个队伍的创建人是谁 ?
        // 如果顺位改创建人，那么如果队伍(比如传播不良信息的队伍)出了问题应该找谁? 出了问题找被迫顺位的那个人?
        Long userId = loginUser.getId();
        if (team.getUserId() == userId) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入自己的队伍里");
        }

        // 第一个锁，锁用户
        // synchronized (String.valueOf(userId).intern()) {
        // 第二个锁，锁队伍
        // 第三个锁，分布式锁
        // 使用redisson分布式锁的特性
        RLock lock = redissonClient.getLock("xiaolu:join_team");
        try {
            // 抢到锁并执行
            while (true) {
                if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                    System.out.println("getLock: " + Thread.currentThread().getId());
                    // 该用户已加入的队伍数量
                    LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(UserTeam::getUserId, userId);
                    long hasJoinNum = userTeamService.count(queryWrapper);
                    if (hasJoinNum > 5) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建和加入 5 个队伍");
                    }
                    // 不能重复加入已加入的队伍
                    queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(UserTeam::getUserId, userId)
                            .eq(UserTeam::getTeamId, teamId);
                    long hasUserJoinTeamNum = userTeamService.count(queryWrapper);
                    if (hasUserJoinTeamNum > 0) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已加入该队伍");
                    }

                    // 已加入队伍的人数，只能加入未满队伍
                    long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
                    if (teamHasJoinNum >= team.getMaxNum()) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满");
                    }

                    // 修改用户队伍关联信息
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error: ", e);
            return false;
        } finally {
            // 释放操作作为必修要做的操作一定要放在finally中，不然一旦上面程序报错，锁就得不到释放了
            // 只能释放自己加的锁
            // 判断当前锁是否为当前线程加的
            if (lock.isHeldByCurrentThread()) {
                // 释放锁
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        // 查询队长是否已加入队伍
        Long userId = loginUser.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(userTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
        }
        long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
        // 队伍只剩一人，解散
        if (teamHasJoinNum == 1) {
            // 删除队伍和所有加入队伍的关系
            this.removeById(teamId);

        } else {// 如果当前队伍大于1人
            // 是否为队长
            if (Objects.equals(team.getUserId(), userId)) {// 是队长
                // 把队伍转移给最早加入的用户
                LambdaQueryWrapper<UserTeam> userTeamQueryWrapper = new LambdaQueryWrapper<>();
                userTeamQueryWrapper.eq(UserTeam::getTeamId, teamId)
                        .orderByAsc(UserTeam::getJoinTime)
                        .last("limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                // 更新当前队伍的队长
                Team updateTeam = new Team();
                // 设置主键id
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队长失败");
                }

            }
        }
        // 移除关系
        return userTeamService.remove(queryWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long teamId, User loginUser) {
        // 校验队伍是否存在
        Team team = getTeamById(teamId);
        // 校验你是不是队伍的队长
        if (!Objects.equals(team.getUserId(), loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        // 移除所有加入队伍的关联信息
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeam::getTeamId, teamId);
        boolean result = userTeamService.remove(queryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
        }
        // 删除队伍
        return this.removeById(teamId);
    }

    /**
     * 根据 队伍id 获取队伍信息
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        return team;
    }

    /**
     * 获取某队伍当前人数
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(long teamId) {
        // 已加入队伍的人数，只能加入未满队伍
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeam::getTeamId, teamId);
        return userTeamService.count(queryWrapper);

    }

}




