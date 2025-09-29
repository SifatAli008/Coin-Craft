package com.coincraft.ui.components;

import com.coincraft.audio.CentralizedMusicManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Centralized Music Controller Component
 * Provides a unified interface for controlling music across all dashboards
 */
public class CentralizedMusicController implements CentralizedMusicManager.MusicStateListener {
    private VBox root;
    private Button playPauseButton;
    private Button muteButton;
    private Slider volumeSlider;
    private Label volumeLabel;
    private Label statusLabel;
    private CentralizedMusicManager musicManager;
    
    public CentralizedMusicController() {
        this.musicManager = CentralizedMusicManager.getInstance();
        this.musicManager.setMusicStateListener(this);
        initializeUI();
        updateControls();
    }
    
    private void initializeUI() {
        root = new VBox(8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(12, 8, 12, 8));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(102, 126, 234, 0.1), rgba(76, 92, 230, 0.05));" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(102, 126, 234, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.2), 8, 0, 0, 2);"
        );
        
        // Title
        Label titleLabel = new Label("🎵 Centralized Music");
        titleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #667EEA;" +
            "-fx-font-weight: 700;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Status label
        statusLabel = new Label("Ready");
        statusLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #4A5568;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Create control buttons
        createControlButtons();
        
        // Create volume controls
        createVolumeControls();
        
        // Add all components
        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(playPauseButton, muteButton);
        
        root.getChildren().addAll(
            titleLabel,
            statusLabel,
            buttonRow,
            volumeLabel,
            volumeSlider
        );
    }
    
    private void createControlButtons() {
        // Play/Pause Button
        playPauseButton = new Button("▶️");
        playPauseButton.setPrefSize(40, 40);
        playPauseButton.setStyle(getButtonStyle());
        
        playPauseButton.setOnAction(e -> {
            musicManager.togglePlayPause();
        });
        
        playPauseButton.setOnMouseEntered(e -> {
            playPauseButton.setStyle(getButtonHoverStyle());
        });
        
        playPauseButton.setOnMouseExited(e -> {
            playPauseButton.setStyle(getButtonStyle());
        });
        
        // Mute Button
        muteButton = new Button("🔊");
        muteButton.setPrefSize(40, 40);
        muteButton.setStyle(getButtonStyle());
        
        muteButton.setOnAction(e -> {
            musicManager.toggleMute();
        });
        
        muteButton.setOnMouseEntered(e -> {
            muteButton.setStyle(getButtonHoverStyle());
        });
        
        muteButton.setOnMouseExited(e -> {
            muteButton.setStyle(getButtonStyle());
        });
    }
    
    private void createVolumeControls() {
        // Volume Label
        double currentVolume = musicManager.getVolume() * 100;
        volumeLabel = new Label("Volume: " + Math.round(currentVolume) + "%");
        volumeLabel.setStyle(getLabelStyle());
        
        // Volume Slider
        volumeSlider = new Slider(0, 100, currentVolume);
        volumeSlider.setPrefWidth(140);
        volumeSlider.setPrefHeight(20);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);
        volumeSlider.setStyle(getSliderStyle());
        
        // Update volume when slider changes
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            musicManager.setVolume(volume);
            volumeLabel.setText("Volume: " + Math.round(newVal.doubleValue()) + "%");
            updateVolumeIcon(volume);
        });
    }
    
    private void updateControls() {
        // Update play/pause button
        if (musicManager.isPlaying()) {
            playPauseButton.setText("⏸️");
            statusLabel.setText("Playing");
            statusLabel.setStyle(statusLabel.getStyle().replace("#4A5568", "#059669"));
        } else {
            playPauseButton.setText("▶️");
            statusLabel.setText("Stopped");
            statusLabel.setStyle(statusLabel.getStyle().replace("#059669", "#4A5568"));
        }
        
        // Update mute button
        if (musicManager.isMuted()) {
            muteButton.setText("🔇");
        } else {
            updateVolumeIcon(musicManager.getVolume());
        }
        
        // Update volume slider
        volumeSlider.setValue(musicManager.getVolume() * 100);
        volumeLabel.setText("Volume: " + Math.round(musicManager.getVolume() * 100) + "%");
    }
    
    private void updateVolumeIcon(double volume) {
        if (musicManager.isMuted()) {
            muteButton.setText("🔇");
        } else if (volume == 0) {
            muteButton.setText("🔇");
        } else if (volume < 0.3) {
            muteButton.setText("🔈");
        } else if (volume < 0.7) {
            muteButton.setText("🔉");
        } else {
            muteButton.setText("🔊");
        }
    }
    
    // MusicStateListener implementation
    @Override
    public void onMusicStateChanged(boolean isPlaying, boolean isMuted, double volume) {
        javafx.application.Platform.runLater(() -> {
            updateControls();
        });
    }
    
    @Override
    public void onTrackChanged(String trackName) {
        javafx.application.Platform.runLater(() -> {
            statusLabel.setText("Track: " + trackName);
        });
    }
    
    private String getButtonStyle() {
        return "-fx-background-color: #667EEA;" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 16px;" +
               "-fx-background-radius: 20;" +
               "-fx-border-radius: 20;" +
               "-fx-cursor: hand;" +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);";
    }
    
    private String getButtonHoverStyle() {
        return "-fx-background-color: #5A67D8;" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 16px;" +
               "-fx-background-radius: 20;" +
               "-fx-border-radius: 20;" +
               "-fx-cursor: hand;" +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 6, 0, 0, 3);" +
               "-fx-scale-x: 1.1; -fx-scale-y: 1.1;";
    }
    
    private String getLabelStyle() {
        return "-fx-font-size: 10px;" +
               "-fx-text-fill: #667EEA;" +
               "-fx-font-weight: 600;" +
               "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;";
    }
    
    private String getSliderStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-control-inner-background: #E0E0E0;" +
               "-fx-accent: #667EEA;" +
               "-fx-focus-color: transparent;" +
               "-fx-faint-focus-color: transparent;";
    }
    
    /**
     * Get the root UI component
     */
    public VBox getRoot() {
        return root;
    }
    
    /**
     * Refresh the controls to match current music state
     */
    public void refresh() {
        updateControls();
    }
}
