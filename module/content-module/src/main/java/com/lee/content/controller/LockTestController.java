package com.lee.content.controller;

import com.lee.content.lock.common.DistributedLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 以下两个测试，分别从两个网站拿的
 */
@Slf4j
@RestController
@RequestMapping("redisson")
@Api(value = "/redisson", tags = {"测试lock用控制层"})
public class LockTestController {

    @Autowired
    private DistributedLock distributedLock;
    /**
     * 锁测试共享变量
     */
    private Integer lockCount = 10;

    /**
     * 无锁测试共享变量
     */
    private Integer count = 10;

    /**
     * 模拟线程数
     */
    private static int threadNum = 10;

    /**
     * 模拟并发测试加锁和不加锁
     * @return
     */
    @GetMapping("/testRedisson2")
    @ApiOperation(value = "模拟并发测试加锁和不加锁")
    public void lock(){
        // 计数器
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < threadNum; i ++) {
            MyRunnable myRunnable = new MyRunnable(countDownLatch);
            Thread myThread = new Thread(myRunnable);
            myThread.start();
        }
        // 释放所有线程
        countDownLatch.countDown();
    }

    /**
     * 加锁测试
     */
    private void testLockCount() {
        String lockKey = "lock-test";
        RLock rLock = null;
        try {
            // 加锁，设置超时时间5s
            rLock = distributedLock.lock(lockKey,5, TimeUnit.SECONDS);
            lockCount--;
            log.info("lockCount值："+lockCount);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            // 释放锁
            distributedLock.unlock(rLock);
        }
    }

    /**
     * 无锁测试
     */
    private void testCount() {
        count--;
        log.info("count值："+count);
    }


    public class MyRunnable implements Runnable {
        /**
         * 计数器
         */
        final CountDownLatch countDownLatch;

        public MyRunnable(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        @SneakyThrows(RuntimeException.class)
        @Override
        public void run() {
            try {
                // 阻塞当前线程，直到计时器的值为0
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
            // 无锁操作
            testCount();
            // 加锁操作
            testLockCount();
        }

    }



    /************************************* 测试分布式锁 redisson *****************************************/
    @GetMapping("/testRedisson")
    public void redissonTest() {
        String key = "redisson_key";
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.err.println("=============线程开启============" + Thread.currentThread().getName());
                        /*
                         * distributedLocker.lock(key,10L); //直接加锁，获取不到锁则一直等待获取锁
                         * Thread.sleep(100); //获得锁之后可以进行相应的处理
                         * System.err.println("======获得锁后进行相应的操作======"+Thread.
                         * currentThread().getName());
                         * distributedLocker.unlock(key); //解锁
                         * System.err.println("============================="+
                         * Thread.currentThread().getName());
                         */
                        RLock lock = distributedLock.tryLock(key, 5L, 10L, TimeUnit.SECONDS); // 尝试获取锁，等待5秒，自己获得锁后一直不解锁则10秒后自动解锁
                        System.out.println("线程:" + Thread.currentThread().getName() + ",获取到了锁");
                        Thread.sleep(2000); // 获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======" + Thread.currentThread().getName());
                        //distributedLock.unlock(lock);
                        System.err.println("线程:" + Thread.currentThread().getName() + "解锁了");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}
