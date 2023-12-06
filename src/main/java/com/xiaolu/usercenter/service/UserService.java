package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolu.usercenter.model.request.UserRegisterRequest;
import com.xiaolu.usercenter.model.vo.UserVO;

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
     * @param userRegisterRequest 用户请求对象
     * @return 新用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

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

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    List<User>  searchUsersByTags(List<String> tagNameList);

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户信息
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
