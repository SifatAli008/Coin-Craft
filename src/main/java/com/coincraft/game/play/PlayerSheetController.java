package com.coincraft.game.play;

import com.coincraft.engine.Updatable;
import com.coincraft.engine.input.InputManager;
import com.coincraft.engine.rendering.Sprite;
import javafx.scene.input.KeyCode;

/**
 * Player controller for spritesheet-based animations (idle/walk).
 * Switches between two Sprite instances (idle and walk) and moves the active node.
 */
public class PlayerSheetController implements Updatable {
    private final InputManager inputManager;
    private final Sprite idleSprite;
    private final Sprite walkSprite;
    private Sprite activeSprite;
    private final double moveSpeedPixelsPerSecond;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final TileCollisionMap collisions;
    
    // Dash mechanics
    private static final double DASH_DURATION_SECONDS = 0.15; // seconds the dash is active
    private static final double DASH_COOLDOWN_SECONDS = 0.60; // seconds before another dash
    private static final double DASH_SPEED_MULTIPLIER = 3.5;  // speed boost while dashing
    private double dashTimeRemaining = 0.0;
    private double dashCooldownRemaining = 0.0;
    private double dashDirX = 0.0;
    private double dashDirY = 0.0;
    private double lastMoveX = 1.0; // default face right
    private double lastMoveY = 0.0;
    private boolean prevSpaceDown = false; // local edge detector (decoupled from InputManager.update)

    public PlayerSheetController(InputManager inputManager,
                                 Sprite idleSprite,
                                 Sprite walkSprite,
                                 double moveSpeedPixelsPerSecond,
                                 double minX,
                                 double minY,
                                 double maxX,
                                 double maxY,
                                 TileCollisionMap collisions) {
        this.inputManager = inputManager;
        this.idleSprite = idleSprite;
        this.walkSprite = walkSprite;
        this.activeSprite = idleSprite;
        this.moveSpeedPixelsPerSecond = moveSpeedPixelsPerSecond;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.collisions = collisions;

        this.idleSprite.setLoopAnimation(true);
        this.walkSprite.setLoopAnimation(true);
        this.idleSprite.startAnimation();
        
        // Ensure only idle sprite is visible initially
        this.idleSprite.getNode().setVisible(true);
        this.walkSprite.getNode().setVisible(false);
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
            activeSprite.setScaleX(-1);
        }
        if (inputManager.isKeyPressed(KeyCode.D) || inputManager.isKeyPressed(KeyCode.RIGHT)) {
            dx += 1;
            activeSprite.setScaleX(1);
        }

        boolean moving = dx != 0 || dy != 0;
        if (moving) { lastMoveX = dx; lastMoveY = dy; }
        
        // Face mouse horizontally (flip by mouse X relative to player center)
        // Convert scene mouse to the local coordinates of the world pane (camera aware)
        javafx.scene.Node parent = activeSprite.getNode().getParent();
        double worldMouseX = inputManager.getMouseX();
        if (parent != null) {
            javafx.geometry.Point2D local = parent.sceneToLocal(inputManager.getMouseX(), inputManager.getMouseY());
            worldMouseX = local.getX();
        }
        double playerCenterX = activeSprite.getX() + Math.max(1.0, activeSprite.getWidth()) * 0.5;
        activeSprite.setScaleX(worldMouseX >= playerCenterX ? 1 : -1);
        Sprite desired = moving ? walkSprite : idleSprite;
        if (desired != activeSprite) {
            // stop previous and start new animation
            activeSprite.stopAnimation();
            desired.startAnimation();
            desired.setScaleX(activeSprite.getScaleX());
            desired.setX(activeSprite.getX());
            desired.setY(activeSprite.getY());
            // toggle visibility
            desired.getNode().setVisible(true);
            activeSprite.getNode().setVisible(false);
            activeSprite = desired;
        }

        // Dash input (Space) - local edge detection to avoid sticky just-pressed
        boolean spaceDown = inputManager.isKeyPressed(KeyCode.SPACE);
        if (spaceDown && !prevSpaceDown && dashCooldownRemaining <= 0.0) {
            dashTimeRemaining = DASH_DURATION_SECONDS;
            dashCooldownRemaining = DASH_COOLDOWN_SECONDS + DASH_DURATION_SECONDS;
            // Use current movement direction, or last direction if stationary
            double ndx = moving ? dx : lastMoveX;
            double ndy = moving ? dy : lastMoveY;
            double len = Math.sqrt(ndx * ndx + ndy * ndy);
            if (len == 0) { ndx = (activeSprite.getScaleX() >= 0) ? 1 : -1; ndy = 0; len = 1; }
            dashDirX = ndx / len;
            dashDirY = ndy / len;
        }

        // Tick dash and cooldown
        if (dashTimeRemaining > 0.0) {
            dashTimeRemaining -= deltaTime;
            if (dashTimeRemaining <= 0.0) { dashDirX = 0.0; dashDirY = 0.0; }
        }
        if (dashCooldownRemaining > 0.0) {
            dashCooldownRemaining -= deltaTime;
        }

        if (moving || dashTimeRemaining > 0.0) {
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > 0) { dx /= len; dy /= len; }
            double usedDx = dx;
            double usedDy = dy;
            double speed = moveSpeedPixelsPerSecond;
            if (dashTimeRemaining > 0.0) {
                usedDx = dashDirX;
                usedDy = dashDirY;
                speed *= DASH_SPEED_MULTIPLIER;
            }
            double dist = speed * deltaTime;
            double nextX = activeSprite.getX() + usedDx * dist;
            double nextY = activeSprite.getY() + usedDy * dist;
            double halfW = Math.max(1.0, activeSprite.getWidth()) * 0.5;
            double halfH = Math.max(1.0, activeSprite.getHeight()) * 0.5;
            // clamp within bounds
            nextX = Math.max(minX, Math.min(maxX - halfW * 2, nextX));
            nextY = Math.max(minY, Math.min(maxY - halfH * 2, nextY));
            double newX = nextX;
            double newY = nextY;
            if (collisions != null) {
                double w = Math.max(1.0, activeSprite.getWidth());
                double h = Math.max(1.0, activeSprite.getHeight());
                // attempt horizontal, then vertical separately for simple resolution
                if (!collisions.isRectBlocked(newX, activeSprite.getY(), w, h)) {
                    activeSprite.setX(newX);
                }
                if (!collisions.isRectBlocked(activeSprite.getX(), newY, w, h)) {
                    activeSprite.setY(newY);
                }
            } else {
                activeSprite.setX(newX);
                activeSprite.setY(newY);
            }
        }

        // advance animation frame and apply transforms for active sprite only
        activeSprite.update(deltaTime);

        // remember key state for next frame
        prevSpaceDown = spaceDown;
    }
}


