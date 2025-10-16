package com.coincraft.engine.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event manager for handling game events
 * Provides a centralized event system for the game engine
 */
public class EventManager {
    private final Map<Class<? extends GameEvent>, List<EventHandler<? extends GameEvent>>> handlers = new ConcurrentHashMap<>();
    private final List<GameEvent> eventQueue = new CopyOnWriteArrayList<>();
    
    /**
     * Register an event handler
     */
    public <T extends GameEvent> void registerHandler(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
    }
    
    /**
     * Unregister an event handler
     */
    public <T extends GameEvent> void unregisterHandler(Class<T> eventType, EventHandler<T> handler) {
        List<EventHandler<? extends GameEvent>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
        }
    }
    
    /**
     * Fire an event
     */
    public void fireEvent(GameEvent event) {
        eventQueue.add(event);
    }
    
    /**
     * Process all queued events
     */
    public void processEvents() {
        for (GameEvent event : eventQueue) {
            processEvent(event);
        }
        eventQueue.clear();
    }
    
    /**
     * Process a single event
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void processEvent(GameEvent event) {
        List<EventHandler<? extends GameEvent>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler handler : eventHandlers) {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    System.err.println("❌ Error handling event: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Clear all handlers
     */
    public void clear() {
        handlers.clear();
        eventQueue.clear();
    }
}
