package com.coincraft.ui.theme;

import com.coincraft.audio.CentralizedMusicManager;

import javafx.scene.control.Button;

/**
 * Custom 8-bit style button with pixel-perfect rendering and retro interactions
 */
public class PixelButton extends Button {
    private boolean isPressed = false;
    
    public PixelButton() {
        super();
        initializePixelButton();
    }
    
    public PixelButton(String text) {
        super(text);
        initializePixelButton();
    }
    
    private void initializePixelButton() {
        // Apply pixel button styling
        getStyleClass().add("pixel-button");
        
        // Add retro sound effects
        setOnMousePressed(e -> {
            isPressed = true;
            // PixelAudio removed
            updatePressedState();
        });
        
        setOnMouseReleased(e -> {
            isPressed = false;
            updatePressedState();
        });
        
        setOnMouseEntered(e -> {
            if (!isPressed) {
                setTranslateY(-1);
            }
        });
        
        setOnMouseExited(e -> {
            if (!isPressed) {
                setTranslateY(0);
            }
        });
    }
    
    private void updatePressedState() {
        if (isPressed) {
            setTranslateY(1);
            setStyle("-fx-background-color: #F57C00;");
        } else {
            setTranslateY(0);
            setStyle("");
        }
    }
    
    /**
     * Create a primary action button with success sound
     */
    public static PixelButton createPrimary(String text, Runnable action) {
        PixelButton button = new PixelButton(text);
        button.setOnAction(e -> {
            CentralizedMusicManager.getInstance().playButtonClick();
            if (action != null) {
                action.run();
            }
        });
        return button;
    }
    
    /**
     * Create a secondary button with standard click sound
     */
    public static PixelButton createSecondary(String text, Runnable action) {
        PixelButton button = new PixelButton(text);
        button.getStyleClass().add("pixel-button-secondary");
        button.setOnAction(e -> {
            CentralizedMusicManager.getInstance().playButtonClick();
            if (action != null) {
                action.run();
            }
        });
        return button;
    }
}
