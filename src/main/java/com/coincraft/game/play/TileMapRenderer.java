package com.coincraft.game.play;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Tilemap renderer that repeats a specific tile (cropped from a tileset)
 * to fill a world area at a desired destination tile size.
 */
public class TileMapRenderer {
    private final Pane targetPane;
    private final Image tileset;
    private final int dstTileWidth;
    private final int dstTileHeight;
    private final Rectangle2D sourceTileRect; // region in tileset to repeat

    /**
     * @param sourceTileRect Rectangle within the tileset (e.g., 16x16 tile region) to repeat
     */
    public TileMapRenderer(Pane targetPane, Image tileset, int dstTileWidth, int dstTileHeight, Rectangle2D sourceTileRect) {
        this.targetPane = targetPane;
        this.tileset = tileset;
        this.dstTileWidth = dstTileWidth;
        this.dstTileHeight = dstTileHeight;
        this.sourceTileRect = sourceTileRect;
    }

    /**
     * Convenience ctor that repeats the whole image (for single-tile images).
     */
    public TileMapRenderer(Pane targetPane, Image singleTile, int dstTileWidth, int dstTileHeight) {
        this(targetPane, singleTile, dstTileWidth, dstTileHeight,
            new Rectangle2D(0, 0, singleTile.getWidth(), singleTile.getHeight()));
    }

    public void fill(int mapWidth, int mapHeight) {
        targetPane.getChildren().clear();
        int cols = Math.max(1, mapWidth / dstTileWidth);
        int rows = Math.max(1, mapHeight / dstTileHeight);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                ImageView iv = new ImageView(tileset);
                iv.setViewport(sourceTileRect);
                iv.setFitWidth(dstTileWidth);
                iv.setFitHeight(dstTileHeight);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);
                iv.setLayoutX(x * dstTileWidth);
                iv.setLayoutY(y * dstTileHeight);
                targetPane.getChildren().add(iv);
            }
        }
    }

    /**
     * Fill with weighted random variants from the same tileset.
     */
    public void fillVariants(int mapWidth, int mapHeight, Rectangle2D[] variants, double[] weights) {
        targetPane.getChildren().clear();
        int cols = Math.max(1, mapWidth / dstTileWidth);
        int rows = Math.max(1, mapHeight / dstTileHeight);
        double[] prefix = new double[weights.length];
        double sum = 0;
        for (int i = 0; i < weights.length; i++) { sum += Math.max(0, weights[i]); prefix[i] = sum; }
        java.util.Random rng = new java.util.Random(42);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double r = rng.nextDouble() * sum;
                int idx = 0;
                while (idx < prefix.length && r > prefix[idx]) idx++;
                if (idx >= variants.length) idx = variants.length - 1;
                ImageView iv = new ImageView(tileset);
                iv.setViewport(variants[idx]);
                iv.setFitWidth(dstTileWidth);
                iv.setFitHeight(dstTileHeight);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);
                iv.setLayoutX(x * dstTileWidth);
                iv.setLayoutY(y * dstTileHeight);
                targetPane.getChildren().add(iv);
            }
        }
    }
}


