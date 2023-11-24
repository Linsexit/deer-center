package com.xiaolu.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolu.usercenter.mapper.UserMapper;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/21 9:59
 * @Description 针对redis的定时任务
 */
@Slf4j
@Component
public class PreCacheJob {

    // 重点用户
    private List<Long> mainUserList = Arrays.asList(9L);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * redis 缓存预热定时任务
     * 每天的23:59:00执行，预热推荐用户  cron表达式指定执行周期
     * 0 59 23 * * * 分别是 秒 分 时 日 月 年
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void doCacheRecommendUser() {
        // 使用redisson分布式锁的特性
        RLock lock = redissonClient.getLock("xiaolu:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            // tryLock 第一个参数是waitTime：即超过等待时间其他线程直接返回false不执行
            // 第二个参数是leaseTime：超时时间，即过期时间，超过超时时间就释放锁。如果设置为-1则是看门狗机制
            // 看门狗机制(过期时间即将过期解决续期问题)：
            //    主要解决执行时间过长导致锁过期提前释放，而当前线程并未执行结束的问题 (蹲坑蹲到一半外面的人把锁给撬开强行进入)
            //    默认过期时间是30秒，每10秒续期一次 (补到30秒, 主要怕宕机)
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format("xiaolu:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 6, TimeUnit.HOURS);
                    } catch (Exception e) {
                        log.error("redis key error", e);
                    }
                }

            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error: ", e);
        } finally {
            // 释放操作作为必修要做的操作一定要放在finally中，不然一旦上面程序报错，锁就得不到释放了
            // 只能释放自己加的锁
            // 判断当前锁是否为当前线程加的
            if (lock.isHeldByCurrentThread()) {
                // 释放锁
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }

        }

    }

}
