package com.coincraft.engine.events;

/**
 * Base class for game events
 */
public abstract class GameEvent {
    private final long timestamp;

    protected GameEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}


