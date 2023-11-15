package com.xiaolu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaolu.usercenter.common.BaseResponse;
import com.xiaolu.usercenter.common.ErrorCode;
import com.xiaolu.usercenter.exception.BusinessException;
import com.xiaolu.usercenter.model.domain.Chat;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.model.domain.request.UserLoginRequest;
import com.xiaolu.usercenter.model.domain.request.UserRegisterRequest;
import com.xiaolu.usercenter.service.ChatService;
import com.xiaolu.usercenter.service.UserService;
import com.xiaolu.usercenter.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiaolu.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.xiaolu.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/28 0:42
 * @Description 用户接口
 */
@RestController
@RequestMapping("/user")
// 允许指定跨域请求通过,
// TODO: 2023/11/7 生产环境不能用这种方式，待优化

// @CrossOrigin(origins = {"http://127.0.0.1:5173","http://localhost:8000"})
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ChatService chatService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN) ;
        }
        Long userId = currentUser.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 鉴权 仅管理员可查询
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<User> list = userService.searchUsers(username);
        return ResultUtils.success(list);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    @GetMapping("/recommend")
    public BaseResponse<List<User>> recommendUsers(HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);

        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request) {
        // 鉴权 仅管理员可查询
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/chat/list")
    public BaseResponse<List<Chat>> chatList(int page, HttpServletRequest request) {

        List<Chat> list = chatService.searchChats(request, page);
        return ResultUtils.success(list);
    }

    @PostMapping("/chat")
    public BaseResponse<Chat> chat(@RequestBody String quest, HttpServletRequest request) {

        Chat chat = chatService.addChat(request, quest);

        return ResultUtils.success(chat);
    }

    @GetMapping("/chat/delChat")
    public BaseResponse<Integer> delChat(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN) ;
        }
        LambdaQueryWrapper<Chat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Chat::getUserid, currentUser.getId());

        if (!chatService.remove(queryWrapper)) {
            throw new BusinessException(ErrorCode.CHAT_REQUEST_ERROR, "清空失败") ;
        }
        return ResultUtils.success(0);
    }



}
