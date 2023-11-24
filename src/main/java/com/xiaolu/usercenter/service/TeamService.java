package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolu.usercenter.model.domain.User;

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


}
