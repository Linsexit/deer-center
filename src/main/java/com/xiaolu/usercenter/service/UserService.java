package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
* @author 16385
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-09-27 11:02:20
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param deerCode 鹿编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String deerCode);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户查询
     * @param username
     * @return
     */
    List<User> searchUsers(String username);

    /**
     * 用户注销
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
