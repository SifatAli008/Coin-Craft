package com.coincraft.game.play;

import com.coincraft.engine.Updatable;
import com.coincraft.engine.rendering.Sprite;

/**
 * Simple camera that follows a sprite by translating a world pane.
 */
public class CameraFollow implements Updatable {
    private final javafx.scene.layout.Pane worldPane;
    private final Sprite target;
    private final double viewportWidth;
    private final double viewportHeight;
    private final double worldWidth;
    private final double worldHeight;

    public CameraFollow(javafx.scene.layout.Pane worldPane,
                        Sprite target,
                        double viewportWidth,
                        double viewportHeight,
                        double worldWidth,
                        double worldHeight) {
        this.worldPane = worldPane;
        this.target = target;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    @Override
    public void update(double deltaTime) {
        double desiredCenterX = target.getX();
        double desiredCenterY = target.getY();

        double tx = -(desiredCenterX - viewportWidth / 2.0);
        double ty = -(desiredCenterY - viewportHeight / 2.0);

        // Clamp so camera doesn't show outside world
        double minTx = -(worldWidth - viewportWidth);
        double minTy = -(worldHeight - viewportHeight);
        if (Double.isNaN(minTx)) minTx = 0;
        if (Double.isNaN(minTy)) minTy = 0;

        tx = Math.max(minTx, Math.min(0, tx));
        ty = Math.max(minTy, Math.min(0, ty));

        worldPane.setTranslateX(tx);
        worldPane.setTranslateY(ty);
    }
}


