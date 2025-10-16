package com.coincraft.game.tile;

import javafx.geometry.Rectangle2D;

/**
 * 4-way bitmask autotile rules (up, right, down, left) -> tileset region.
 * Bit order: 1=Up, 2=Right, 4=Down, 8=Left.
 */
public final class AutoTileRules {
    private final int srcTileSize;
    private final int originCol;
    private final int originRow;

    /**
     * @param srcTileSize size of a source tile cell in the tileset (e.g., 16)
     * @param originCol column in the tileset where the rule block starts
     * @param originRow row in the tileset where the rule block starts
     */
    public AutoTileRules(int srcTileSize, int originCol, int originRow) {
        this.srcTileSize = srcTileSize;
        this.originCol = originCol;
        this.originRow = originRow;
    }

    /**
     * Map a mask to a tileset region. This mapping is illustrative; tweak to match your atlas.
     */
    public Rectangle2D regionForMask(int mask) {
        int col = originCol;
        int row = originRow;
        // rule-switch for cleaner mapping
        switch (mask) {
            case 0  -> { col += 1; row += 1; } // isolated
            case 1  -> { col += 1; row += 0; } // connected up
            case 2  -> { col += 2; row += 1; } // connected right
            case 4  -> { col += 1; row += 2; } // connected down
            case 8  -> { col += 0; row += 1; } // connected left
            case 3  -> { col += 2; row += 0; } // up+right corner
            case 6  -> { col += 2; row += 2; } // right+down corner
            case 12 -> { col += 0; row += 2; } // down+left corner
            case 9  -> { col += 0; row += 0; } // left+up corner
            case 5  -> { col += 1; row += 3; } // up+down vertical
            case 10 -> { col += 3; row += 1; } // left+right horizontal
            case 7  -> { col += 3; row += 0; } // T up-right-down
            case 14 -> { col += 3; row += 2; } // T right-down-left
            case 13 -> { col += 0; row += 3; } // T down-left-up
            case 11 -> { col += 2; row += 3; } // T left-up-right
            case 15 -> { col += 1; row += 1; } // cross
            default -> { col += 1; row += 1; }
        }
        return new Rectangle2D(col * srcTileSize, row * srcTileSize, srcTileSize, srcTileSize);
    }
}


