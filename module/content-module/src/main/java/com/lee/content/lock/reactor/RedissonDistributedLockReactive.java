package com.lee.content.lock.reactor;

import com.lee.content.constant.CommonConstant;
import com.lee.content.exception.LockRuntimeException;
import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * redisson分布式锁实现，基本锁功能的抽象实现
 * 本接口能满足绝大部分的需求，高级的锁功能，请自行扩展或直接使用原生api
 *
 * @author lbj
 * @date 2020/6/5
 * <p>
 */
@Component
public class RedissonDistributedLockReactive implements DistributedLockReactive {
    @Autowired
    private RedissonReactiveClient redissonReactiveClient;

    private RLockReactive getLock(String key, boolean isFair) {
        if (isFair) {
            return redissonReactiveClient.getFairLock(CommonConstant.LOCK_KEY_PREFIX + key);
        }
        return redissonReactiveClient.getLock(CommonConstant.LOCK_KEY_PREFIX + key);
    }

    @Override
    public Mono<RLockReactive> lock(String key, long leaseTime, TimeUnit unit, boolean isFair) {
        return Mono
                .fromCallable(() -> getLock(key, isFair))
                .map(lock -> {
                    lock.lock(leaseTime, unit);
                    return lock;
        });

    }
    @Override
    public Mono<RLockReactive> lock(String key, long leaseTime, TimeUnit unit) {
        return lock(key, leaseTime, unit, false);
    }
    @Override
    public Mono<RLockReactive> lock(String key, boolean isFair) {
        return lock(key, -1, null, isFair);
    }
    @Override
    public Mono<RLockReactive> lock(String key) {
        return lock(key, -1, null, false);
    }

    @Override
    public Mono<RLockReactive> tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws InterruptedException {
        return Mono
                .justOrEmpty(getLock(key, isFair))
                .flatMap(lock -> lock.tryLock(waitTime, leaseTime, unit).map(isGetLock -> {
                    if(isGetLock){
                        return lock;
                    }else{
                        return null;
                    }
                }));
    }
    @Override
    public Mono<RLockReactive> tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return tryLock(key, waitTime, leaseTime, unit, false);
    }
    @Override
    public Mono<RLockReactive> tryLock(String key, long waitTime, TimeUnit unit, boolean isFair) throws InterruptedException {
        return tryLock(key, waitTime, -1, unit, isFair);
    }
    @Override
    public Mono<RLockReactive> tryLock(String key, long waitTime, TimeUnit unit) throws InterruptedException {
        return tryLock(key, waitTime, -1, unit, false);
    }

    @Override
    public Mono<Void> unlock(Object lock) {
        if (lock != null) {
            if (lock instanceof RLockReactive) {
                RLockReactive rLock = (RLockReactive)lock;
                rLock.isLocked().flatMap(locked -> {
                    if(locked){
                        return rLock.unlock();
                    }
                    return Mono.empty();
                });
            } else {
                throw new LockRuntimeException("unlock RLock fail.");
            }
        }
        return Mono.empty();
    }
}
