package com.coincraft.engine.input;

import javafx.scene.input.KeyCode;

/**
 * Represents an input event
 */
public class InputEvent {
    public enum Type {
        KEY_PRESSED,
        KEY_RELEASED,
        MOUSE_MOVED,
        MOUSE_PRESSED,
        MOUSE_RELEASED,
        MOUSE_CLICKED
    }
    
    private final Type type;
    private final KeyCode keyCode;
    private final double x;
    private final double y;
    private final long timestamp;
    
    public InputEvent(Type type, KeyCode keyCode, double x, double y) {
        this.type = type;
        this.keyCode = keyCode;
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Type getType() {
        return type;
    }
    
    public KeyCode getKeyCode() {
        return keyCode;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isKeyEvent() {
        return type == Type.KEY_PRESSED || type == Type.KEY_RELEASED;
    }
    
    public boolean isMouseEvent() {
        return type == Type.MOUSE_MOVED || type == Type.MOUSE_PRESSED || 
               type == Type.MOUSE_RELEASED || type == Type.MOUSE_CLICKED;
    }
}
