package com.coincraft.game.play;

/**
 * Simple tile-based collision map.
 */
public class TileCollisionMap {
    private final boolean[][] blocked; // [row][col]
    private final int tileSize;
    private final int cols;
    private final int rows;

    public TileCollisionMap(int cols, int rows, int tileSize) {
        this.cols = cols;
        this.rows = rows;
        this.tileSize = tileSize;
        this.blocked = new boolean[rows][cols];
    }

    public int getTileSize() { return tileSize; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public void setBlocked(int col, int row, boolean value) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            blocked[row][col] = value;
        }
    }

    public boolean isBlockedTile(int col, int row) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return true;
        return blocked[row][col];
    }

    /**
     * Check if a rectangle intersects any blocked tiles.
     * For player movement we only consider the tile under the player's center
     * so the player is blocked only when stepping onto the blocked tile itself.
     */
    public boolean isRectBlocked(double x, double y, double width, double height) {
        int centerCol = (int) Math.floor((x + width * 0.5) / tileSize);
        int centerRow = (int) Math.floor((y + height * 0.5) / tileSize);
        return isBlockedTile(centerCol, centerRow);
    }
}


