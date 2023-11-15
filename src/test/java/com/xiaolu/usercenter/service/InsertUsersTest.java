package com.xiaolu.usercenter.service;

import cn.hutool.core.date.StopWatch;
import com.xiaolu.usercenter.mapper.UserMapper;
import com.xiaolu.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/14 1:36
 * @Description
 */
@SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserService userService;

    /**
     * 批量插入用户到数据库
     */
    @Test
    public void doInsertUsers() {
        // spring 提供的一个倒计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假晓鹿");
            user.setUserAccount("fakedeer");
            user.setAvatarUrl("https://tse4-mm.cn.bing.net/th/id/OIP-C.ONUUhGF5kYwGjbPTegp4GQAAAA");
            user.setGender(0);
            user.setUserPassword("123456");
            user.setPhone("123");
            user.setEmail("123@gmail.com");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setDeerCode("11111");
            user.setTotalPage(0L);
            userList.add(user);
        }
        userService.saveBatch(userList, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

    /**
     * 并发批量插入用户到数据库
     */
    @Test
    public void doConcurrencyInsertUsers() {
        // spring 提供的一个倒计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假晓鹿");
            user.setUserAccount("fakedeer");
            user.setAvatarUrl("https://tse4-mm.cn.bing.net/th/id/OIP-C.ONUUhGF5kYwGjbPTegp4GQAAAA");
            user.setGender(0);
            user.setUserPassword("123456");
            user.setPhone("123");
            user.setEmail("123@gmail.com");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setDeerCode("11111");
            user.setTotalPage(0L);
            userList.add(user);
        }
        userService.saveBatch(userList, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

}
