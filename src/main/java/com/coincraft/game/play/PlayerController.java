package com.coincraft.game.play;

import com.coincraft.engine.Updatable;
import com.coincraft.engine.input.InputManager;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * Minimal player controller that moves a sprite with keyboard input.
 */
public class PlayerController implements Updatable {
    private final InputManager inputManager;
    private final Node playerNode;
    private final double moveSpeedPixelsPerSecond;

    public PlayerController(InputManager inputManager, Node playerNode, double moveSpeedPixelsPerSecond) {
        this.inputManager = inputManager;
        this.playerNode = playerNode;
        this.moveSpeedPixelsPerSecond = moveSpeedPixelsPerSecond;
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
        }
        if (inputManager.isKeyPressed(KeyCode.D) || inputManager.isKeyPressed(KeyCode.RIGHT)) {
            dx += 1;
        }

        if (dx == 0 && dy == 0) {
            return;
        }

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        double distance = moveSpeedPixelsPerSecond * deltaTime;
        playerNode.setLayoutX(playerNode.getLayoutX() + dx * distance);
        playerNode.setLayoutY(playerNode.getLayoutY() + dy * distance);
    }
}


