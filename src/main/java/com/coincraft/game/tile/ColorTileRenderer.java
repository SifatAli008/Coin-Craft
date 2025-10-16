package com.coincraft.game.tile;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Simple tile renderer that draws solid-color rectangles instead of images.
 */
public final class ColorTileRenderer {
    private final Pane targetPane;
    private final int tileSize;

    public ColorTileRenderer(Pane targetPane, int tileSize) {
        this.targetPane = targetPane;
        this.tileSize = tileSize;
    }

    public void fillSolid(int cols, int rows, Color color) {
        targetPane.getChildren().clear();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Rectangle rect = new Rectangle(tileSize, tileSize, color);
                rect.setLayoutX(c * tileSize);
                rect.setLayoutY(r * tileSize);
                targetPane.getChildren().add(rect);
            }
        }
    }

    public void renderBooleanGrid(boolean[][] grid, Color color) {
        if (grid == null || grid.length == 0) return;
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!grid[r][c]) continue;
                Rectangle rect = new Rectangle(tileSize, tileSize, color);
                rect.setLayoutX(c * tileSize);
                rect.setLayoutY(r * tileSize);
                targetPane.getChildren().add(rect);
            }
        }
    }
}


