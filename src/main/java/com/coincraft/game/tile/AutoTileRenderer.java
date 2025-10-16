package com.coincraft.game.tile;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Renders a path layer using 4-way autotiling onto a world pane.
 */
public class AutoTileRenderer {
    private final Pane worldPane;
    private final Image tileset;
    private final int dstTileSize;
    private final AutoTileRules rules;
    private final boolean singleCell;

    public AutoTileRenderer(Pane worldPane, Image tileset, int dstTileSize, AutoTileRules rules) {
        this.worldPane = worldPane;
        this.tileset = tileset;
        this.dstTileSize = dstTileSize;
        this.rules = rules;
        this.singleCell = false;
    }

    public AutoTileRenderer(Pane worldPane, Image tileset, int dstTileSize, AutoTileRules rules, boolean singleCell) {
        this.worldPane = worldPane;
        this.tileset = tileset;
        this.dstTileSize = dstTileSize;
        this.rules = rules;
        this.singleCell = singleCell;
    }

    /**
     * @param path grid: true means path tile present
     */
    public void render(boolean[][] path) {
        int rows = path.length;
        int cols = path[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!path[r][c]) continue;
                Rectangle2D src;
                if (singleCell) {
                    src = new Rectangle2D(rulesForSingleCol(), rulesForSingleRow(), rulesSrcSize(), rulesSrcSize());
                } else {
                    int mask = 0;
                    if (r > 0 && path[r-1][c]) mask |= 1;          // up
                    if (c < cols-1 && path[r][c+1]) mask |= 2;      // right
                    if (r < rows-1 && path[r+1][c]) mask |= 4;      // down
                    if (c > 0 && path[r][c-1]) mask |= 8;           // left
                    src = rules.regionForMask(mask);
                }
                ImageView iv = new ImageView(tileset);
                iv.setViewport(src);
                iv.setFitWidth(dstTileSize);
                iv.setFitHeight(dstTileSize);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);
                iv.setLayoutX(c * dstTileSize);
                iv.setLayoutY(r * dstTileSize);
                worldPane.getChildren().add(iv);
            }
        }
    }

    // Helper functions for single-cell mode using rules' origin and size
    private double rulesForSingleCol() { return 0; }
    private double rulesForSingleRow() { return 0; }
    private double rulesSrcSize() { return rules == null ? 16 : 16; }
}


