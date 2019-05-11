package com.vinz243.tesacore;

public class UsageTracker {
    private long lastUsed;

    public UsageTracker() {
        this.lastUsed = System.currentTimeMillis();
    }

    public void refresh () {
        this.lastUsed = System.currentTimeMillis();
    }

    public long getTimeDelta () {
        return System.currentTimeMillis() - lastUsed;
    }
}
