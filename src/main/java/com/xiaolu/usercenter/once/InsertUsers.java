package com.xiaolu.usercenter.once;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.StopWatch;
import com.xiaolu.usercenter.mapper.UserMapper;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/14 1:13
 * @Description 批量插入1000w条数据的定时任务
 */
@Component
public class InsertUsers {

    @Resource
    private UserService userService;

    /**
     * 批量插入用户到数据库
     * Scheduled 定时任务
     */
    // @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {
        // spring 提供的一个倒计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
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
        userService.saveBatch(userList, 100);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

}
