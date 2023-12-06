package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.model.dto.TeamQuery;
import com.xiaolu.usercenter.model.request.TeamJoinRequest;
import com.xiaolu.usercenter.model.request.TeamQuitRequest;
import com.xiaolu.usercenter.model.request.TeamUpdateRequest;
import com.xiaolu.usercenter.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 16385
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-11-24 10:34:29
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, Boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    Boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除（解散）队伍
     * @param teamId
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long teamId, User loginUser);
}
