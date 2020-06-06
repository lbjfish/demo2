package com.lee.content.lock.red;

import com.lee.content.constant.CommonConstant;
import com.lee.content.exception.LockRuntimeException;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
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
public class RedissonDistributedRedLock implements DistributedRedLock {

    @Value("${content.addres}")
    private String[] addres;

    /**
     * 红锁 RedLock 实现
     *  注意：
     *      1.RedLock 必须是在cluster集群模式下使用（就算是集群，也要把集群当做6台单节点用useSingleServer，而不是useClusterServers）
     *      2.加入集群6个节点（包含slave），那么就要实现6个 Config config1-6 = new Config();
     * @param address
     */
    public RedissonRedLock getRedLock(String key, boolean isFair, String... address) {
        if(Objects.equals(address.length, 0)){
            throw new LockRuntimeException("address must not null.");
        }
        RLock[] rLockArrs = new RLock[address.length];
        for (int i = 0; i < address.length; i++) {
            RLock lock;
            Config config = new Config();
            config.useSingleServer().setAddress(address[i]);
            RedissonClient redissonClient = Redisson.create(config);
            if (isFair) {
                lock = redissonClient.getFairLock(CommonConstant.LOCK_KEY_PREFIX + key);
            }else{
                lock = redissonClient.getLock(key);
            }
            rLockArrs[i] = Optional.ofNullable(lock).orElseThrow(() -> new LockRuntimeException("One machine lock is empty"));
        }

        RedissonRedLock redLock = new RedissonRedLock(rLockArrs);
        return redLock;
    }

    @Override
    public RedissonRedLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        RedissonRedLock redLock = getRedLock(key, isFair, addres);
        redLock.lock(leaseTime, unit);
        return null;
    }

    @Override
    public RedissonRedLock lock(String key, long leaseTime, TimeUnit unit) throws Exception {
        return lock(key, leaseTime, unit, false);
    }

    @Override
    public RedissonRedLock lock(String key, boolean isFair) throws Exception {
        return lock(key, -1, null, isFair);
    }

    @Override
    public RedissonRedLock lock(String key) throws Exception {
        return lock(key, -1, null, false);
    }

    @Override
    public RedissonRedLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        RedissonRedLock redLock = getRedLock(key, isFair, addres);
        if (redLock.tryLock(waitTime, leaseTime, unit)) {
            return redLock;
        }
        return null;
    }

    @Override
    public RedissonRedLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws Exception {
        return tryLock(key, waitTime, leaseTime, unit, false);
    }

    @Override
    public RedissonRedLock tryLock(String key, long waitTime, TimeUnit unit, boolean isFair) throws Exception {
        return tryLock(key, waitTime, -1, unit, isFair);
    }

    @Override
    public RedissonRedLock tryLock(String key, long waitTime, TimeUnit unit) throws Exception {
        return tryLock(key, waitTime, -1, unit, false);
    }

    @Override
    public void unlock(Object lock) {
        if (lock != null) {
            if (lock instanceof RedissonRedLock) {
                RedissonRedLock rLock = (RedissonRedLock)lock;
                rLock.unlock();
            } else {
                throw new LockRuntimeException("unlock RLock fail.");
            }
        }
    }
}
