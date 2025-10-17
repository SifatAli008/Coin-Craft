package com.coincraft.engine.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Input manager for handling keyboard and mouse input
 * Provides centralized input handling for the game engine
 * Updated with mouse button support for right-click attack
 */
public class InputManager {
    private final Scene scene;
    
    // Input state tracking
    private final Map<KeyCode, Boolean> keyStates = new HashMap<>();
    private final Map<KeyCode, Boolean> keyPressed = new HashMap<>();
    private final Map<KeyCode, Boolean> keyReleased = new HashMap<>();
    
    // Mouse state
    private double mouseX = 0;
    private double mouseY = 0;
    private boolean mousePressed = false;
    private boolean mouseReleased = false;
    private boolean mouseClicked = false;
    // Button-specific mouse state
    private boolean primaryPressed = false;
    private boolean primaryJustPressed = false;
    private boolean primaryReleased = false;
    private boolean primaryClicked = false;
    private boolean secondaryPressed = false;
    private boolean secondaryJustPressed = false;
    private boolean secondaryReleased = false;
    private boolean secondaryClicked = false;
    
    // Input handlers
    private final List<InputHandler> handlers = new CopyOnWriteArrayList<>();
    
    // Input events
    private final List<InputEvent> inputEvents = new ArrayList<>();
    
    public InputManager(Scene scene) {
        this.scene = scene;
        setupInputHandlers();
    }
    
    /**
     * Setup input event handlers
     */
    private void setupInputHandlers() {
        // Keyboard events
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
        
        // Mouse events
        scene.setOnMouseMoved(this::handleMouseMoved);
        scene.setOnMousePressed(this::handleMousePressed);
        scene.setOnMouseReleased(this::handleMouseReleased);
        scene.setOnMouseClicked(this::handleMouseClicked);
    }
    
    /**
     * Update input manager
     */
    public void update(double deltaTime) {
        // Clear single-frame events
        keyPressed.clear();
        keyReleased.clear();
        mouseReleased = false;
        mouseClicked = false;
        primaryJustPressed = false;
        primaryReleased = false;
        primaryClicked = false;
        secondaryJustPressed = false;
        secondaryReleased = false;
        secondaryClicked = false;
        
        // Process input events
        for (InputEvent event : inputEvents) {
            for (InputHandler handler : handlers) {
                handler.handleInput(event);
            }
        }
        inputEvents.clear();
    }
    
    /**
     * Handle key pressed event
     */
    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        keyStates.put(keyCode, true);
        keyPressed.put(keyCode, true);
        
        inputEvents.add(new InputEvent(InputEvent.Type.KEY_PRESSED, keyCode, 0, 0));
    }
    
    /**
     * Handle key released event
     */
    private void handleKeyReleased(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        keyStates.put(keyCode, false);
        keyReleased.put(keyCode, true);
        
        inputEvents.add(new InputEvent(InputEvent.Type.KEY_RELEASED, keyCode, 0, 0));
    }
    
    /**
     * Handle mouse moved event
     */
    private void handleMouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        
        inputEvents.add(new InputEvent(InputEvent.Type.MOUSE_MOVED, null, mouseX, mouseY));
    }
    
    /**
     * Handle mouse pressed event
     */
    private void handleMousePressed(MouseEvent event) {
        mousePressed = true;
        mouseX = event.getX();
        mouseY = event.getY();
        if (event.getButton() == MouseButton.PRIMARY) {
            primaryPressed = true;
            primaryJustPressed = true;
        } else if (event.getButton() == MouseButton.SECONDARY) {
            secondaryPressed = true;
            secondaryJustPressed = true;
        }
        
        inputEvents.add(new InputEvent(InputEvent.Type.MOUSE_PRESSED, null, mouseX, mouseY));
    }
    
    /**
     * Handle mouse released event
     */
    private void handleMouseReleased(MouseEvent event) {
        mousePressed = false;
        mouseReleased = true;
        mouseX = event.getX();
        mouseY = event.getY();
        if (event.getButton() == MouseButton.PRIMARY) {
            primaryPressed = false;
            primaryReleased = true;
        } else if (event.getButton() == MouseButton.SECONDARY) {
            secondaryPressed = false;
            secondaryReleased = true;
        }
        
        inputEvents.add(new InputEvent(InputEvent.Type.MOUSE_RELEASED, null, mouseX, mouseY));
    }
    
    /**
     * Handle mouse clicked event
     */
    private void handleMouseClicked(MouseEvent event) {
        mouseClicked = true;
        mouseX = event.getX();
        mouseY = event.getY();
        if (event.getButton() == MouseButton.PRIMARY) {
            primaryClicked = true;
        } else if (event.getButton() == MouseButton.SECONDARY) {
            secondaryClicked = true;
        }
        
        inputEvents.add(new InputEvent(InputEvent.Type.MOUSE_CLICKED, null, mouseX, mouseY));
    }
    
    // Keyboard input methods
    public boolean isKeyPressed(KeyCode keyCode) {
        return keyStates.getOrDefault(keyCode, false);
    }
    
    public boolean isKeyJustPressed(KeyCode keyCode) {
        return keyPressed.getOrDefault(keyCode, false);
    }
    
    public boolean isKeyJustReleased(KeyCode keyCode) {
        return keyReleased.getOrDefault(keyCode, false);
    }
    
    // Mouse input methods
    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
    public boolean isMousePressed() { return mousePressed; }
    public boolean isMouseJustPressed() { return mousePressed && !mouseReleased; }
    public boolean isMouseJustReleased() { return mouseReleased; }
    public boolean isMouseClicked() { return mouseClicked; }
    // Button-specific mouse helpers
    public boolean isPrimaryMousePressed() { return primaryPressed; }
    public boolean isPrimaryMouseJustPressed() { return primaryJustPressed; }
    public boolean isPrimaryMouseJustReleased() { return primaryReleased; }
    public boolean isPrimaryMouseClicked() { return primaryClicked; }
    public boolean isSecondaryMousePressed() { return secondaryPressed; }
    public boolean isSecondaryMouseJustPressed() { return secondaryJustPressed; }
    public boolean isSecondaryMouseJustReleased() { return secondaryReleased; }
    public boolean isSecondaryMouseClicked() { return secondaryClicked; }
    
    // Input handler management
    public void addInputHandler(InputHandler handler) {
        handlers.add(handler);
    }
    
    public void removeInputHandler(InputHandler handler) {
        handlers.remove(handler);
    }
    
    // Get all pressed keys
    public Set<KeyCode> getPressedKeys() {
        Set<KeyCode> pressedKeys = new HashSet<>();
        for (Map.Entry<KeyCode, Boolean> entry : keyStates.entrySet()) {
            if (entry.getValue()) {
                pressedKeys.add(entry.getKey());
            }
        }
        return pressedKeys;
    }
}
