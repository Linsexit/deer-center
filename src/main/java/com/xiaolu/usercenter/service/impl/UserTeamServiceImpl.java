package com.xiaolu.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolu.usercenter.model.domain.UserTeam;
import com.xiaolu.usercenter.service.UserTeamService;
import com.xiaolu.usercenter.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 16385
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2023-11-24 10:36:45
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




