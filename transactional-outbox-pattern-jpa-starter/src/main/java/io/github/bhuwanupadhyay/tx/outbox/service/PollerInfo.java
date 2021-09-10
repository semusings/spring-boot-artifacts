package io.github.bhuwanupadhyay.tx.outbox.service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class PollerInfo {

    private final AtomicLong count = new AtomicLong(0L);
    private final AtomicBoolean nextRun = new AtomicBoolean(false);

    public void reset() {
        this.count.set(0L);
        this.nextRun.set(true);
    }

    public boolean hasNextRun() {
        return this.nextRun.get();
    }

    public void addCount(int count) {
        this.count.addAndGet(count);
    }

    public void setNextRun(boolean run) {
        this.nextRun.set(run);
    }

    public boolean hasAnySent() {
        return this.count.get() > 0;
    }

    public long getCount() {
        return this.count.get();
    }

    public void offNextRun() {
        this.nextRun.set(false);
    }
}
