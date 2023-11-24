package com.xiaolu.usercenter.service;

import cn.hutool.core.date.StopWatch;
import com.xiaolu.usercenter.mapper.UserMapper;
import com.xiaolu.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/14 1:36
 * @Description
 */
// @SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserService userService;

    // 线程池
    // CPU 密集型: 建议分配的核心线程数 = CPU - 1
    // IO 密集型: 分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户到数据库
     */
    // @Test
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
    // @Test
    public void doConcurrencyInsertUsers() {
        // spring 提供的一个倒计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分二十组
        int batchSize = 5000;  // 每5000条一组
        int j = 0;
        // 定义一个任务数组
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
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
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 新建异步任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            }, executorService);
            // 拿到了十个异步任务
            futureList.add(future);
        }
        // 执行 , 后面加上join是为了阻塞线程
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 加了join后程序会等所有线程都执行结束后才会执行 stopWatch.stop();代码

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

}
