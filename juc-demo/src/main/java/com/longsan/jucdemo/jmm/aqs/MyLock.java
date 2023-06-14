package com.longsan.jucdemo.jmm.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyLock extends AbstractQueuedSynchronizer {

    @Override
    protected boolean tryAcquire(int unused) {
        // 通过cas加锁 默认状态是0
        if (compareAndSetState(0, 1)) {
            // 标记当前线程
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int unused) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    public void lock() {
        acquire(1);
    }

    public boolean tryLock() {
        return tryAcquire(1);
    }

    public void unLock() {
        release(1);
    }

    public boolean isLocked() {
        return isHeldExclusively();
    }
}
