package com.xiaolu.usercenter.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/23 10:17
 * @Description Redisson 能够让开发者像使用本地集合一样操作redis
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {

        // 像操作list一样操作redis
        // getList方法需要指定一个name (其实就是redis 中的key)
        RList<String> rList = redissonClient.getList("test-list");
        rList.add("xiaolu");
        // rList.get(0);
        System.out.println("rList: " + rList.get(0));
        // rList.remove(0);

        // 像操作map一样操作redis
        RMap<String, String> rMap = redissonClient.getMap("test-map");
        // rMap.put("name", "xiaolu");

        // 像操作set 一样操作redis

        // 像操作stack一样操作redis

    }

}
