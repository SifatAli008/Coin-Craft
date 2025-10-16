package com.coincraft.game.play;

import com.coincraft.engine.Updatable;
import com.coincraft.engine.input.InputManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * Player controller that moves an ImageView and swaps sprite images
 * between idle and walk based on current input state.
 */
public class PlayerSpriteController implements Updatable {
    private final InputManager inputManager;
    private final ImageView playerView;
    private final double moveSpeedPixelsPerSecond;
    private final Image idleImage;
    private final Image walkImage;

    private boolean wasMoving = false;

    public PlayerSpriteController(InputManager inputManager,
                                  ImageView playerView,
                                  double moveSpeedPixelsPerSecond,
                                  Image idleImage,
                                  Image walkImage) {
        this.inputManager = inputManager;
        this.playerView = playerView;
        this.moveSpeedPixelsPerSecond = moveSpeedPixelsPerSecond;
        this.idleImage = idleImage;
        this.walkImage = walkImage;
        this.playerView.setImage(idleImage);
        this.playerView.setPreserveRatio(true);
        this.playerView.setSmooth(true);
    }

    @Override
    public void update(double deltaTime) {
        double dx = 0;
        double dy = 0;

        if (inputManager.isKeyPressed(KeyCode.W) || inputManager.isKeyPressed(KeyCode.UP)) {
            dy -= 1;
        }
        if (inputManager.isKeyPressed(KeyCode.S) || inputManager.isKeyPressed(KeyCode.DOWN)) {
            dy += 1;
        }
        if (inputManager.isKeyPressed(KeyCode.A) || inputManager.isKeyPressed(KeyCode.LEFT)) {
            dx -= 1;
            playerView.setScaleX(-1); // flip horizontally when moving left
        }
        if (inputManager.isKeyPressed(KeyCode.D) || inputManager.isKeyPressed(KeyCode.RIGHT)) {
            dx += 1;
            playerView.setScaleX(1); // normal orientation for right
        }

        boolean isMoving = (dx != 0 || dy != 0);
        if (isMoving && !wasMoving) {
            playerView.setImage(walkImage);
        } else if (!isMoving && wasMoving) {
            playerView.setImage(idleImage);
        }
        wasMoving = isMoving;

        if (!isMoving) {
            return;
        }

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        double distance = moveSpeedPixelsPerSecond * deltaTime;
        playerView.setLayoutX(playerView.getLayoutX() + dx * distance);
        playerView.setLayoutY(playerView.getLayoutY() + dy * distance);
    }
}


