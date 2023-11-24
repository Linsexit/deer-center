package com.xiaolu.usercenter.service;
import java.util.Date;

import com.xiaolu.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/21 9:02
 * @Description
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 增
        valueOperations.set("xiaoString", "cat");
        // valueOperations.set("xiaoInt", 1);
        // valueOperations.set("xiaoDouble", 2.0);
        // User user = new User();
        // user.setId(1L);
        // user.setUsername("sss");
        // valueOperations.set("xiaoUser", user);

        // 查
        Object xiaoString = valueOperations.get("xiaoString");
        Assertions.assertTrue("cat".equals((String) xiaoString));

        // 删
        redisTemplate.delete("xiaoString");

    }

    // @Test
    void testDel() {
        Boolean delete = redisTemplate.delete("xiaolu:user:recommend:9");
        System.out.println("是否删除：" + delete);
    }
}
