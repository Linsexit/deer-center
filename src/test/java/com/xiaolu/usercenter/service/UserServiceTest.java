package com.xiaolu.usercenter.service;

import java.util.Date;

import com.xiaolu.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/27 11:09
 * @Description 用户服务测试
 */
// @SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    // @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("xiaolu");
        user.setUserAccount("2233");
        user.setAvatarUrl("https://ssdad/wada/d.jpg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    // @Test
    // void userRegister() {
    //     String userAccount = "xiaolu2";
    //     String userPassword = "123456789";
    //     String checkPassword = "123456789";
    //     String deerCode = "1";
    //
    //     long result = userService.userRegister(userAccount, userPassword, checkPassword, deerCode);
    //     Assertions.assertTrue(result > 0);
    //
    // }
}