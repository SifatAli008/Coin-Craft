package com.coincraft.engine.input;

/**
 * Interface for input handlers
 * Classes that want to handle input events should implement this interface
 */
public interface InputHandler {
    /**
     * Handle an input event
     * @param event The input event to handle
     */
    void handleInput(InputEvent event);
}
