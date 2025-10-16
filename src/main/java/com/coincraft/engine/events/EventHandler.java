package com.coincraft.engine.events;

/**
 * Functional interface for handling game events
 */
@FunctionalInterface
public interface EventHandler<T extends GameEvent> {
    void handle(T event);
}


