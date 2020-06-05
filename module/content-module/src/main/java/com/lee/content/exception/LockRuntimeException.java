package com.lee.content.exception;

/**
 * 分布式锁异常
 *
 * @author lbj
 */
public class LockRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    public LockRuntimeException(String message) {
        super(message);
    }
}
