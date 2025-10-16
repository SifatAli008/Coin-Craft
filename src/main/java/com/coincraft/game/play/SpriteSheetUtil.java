package com.coincraft.game.play;

import com.coincraft.engine.rendering.Sprite;
import javafx.scene.image.Image;

/**
 * Helper to create a Sprite from a sprite sheet laid out in a single row
 * of square frames where each frame size equals the image height.
 */
public final class SpriteSheetUtil {
    private SpriteSheetUtil() {}

    public static Sprite createSquareRowSprite(Image sheet) {
        int frameSize = (int) sheet.getHeight();
        if (frameSize <= 0) {
            frameSize = (int) Math.min(sheet.getWidth(), sheet.getHeight());
        }
        int frames = (int) Math.max(1, Math.floor(sheet.getWidth() / frameSize));
        Sprite sprite = new Sprite(sheet, frameSize, frameSize, frames);
        sprite.setSize(96, 96); // matches scaled 48x48 tiles at ~2 frames height
        sprite.setLoopAnimation(true);
        sprite.setAnimationSpeed(12.0); // 12 fps default
        return sprite;
    }
}


