package com.coincraft.game.tile;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal CSV tilemap loader compatible with Tiled (CSV layer export).
 * Assumptions:
 * - Single tileset image, grid-ordered, 0 = empty, 1 = first tile (top-left),
 *   then increment left-to-right, top-to-bottom.
 * - Source tiles are square of size srcTileSize.
 */
public final class TiledCsvMapLoader {
    public static int[][] loadCsv(Path csvPath) throws IOException {
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                int[] row = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    String p = parts[i].trim();
                    if (p.endsWith("\r")) p = p.substring(0, p.length()-1);
                    row[i] = p.isEmpty() ? 0 : Integer.parseInt(p);
                }
                rows.add(row);
            }
        }
        return rows.toArray(new int[0][]);
    }

    public static void renderLayer(Pane target, Image tileset, int[][] gids, int srcTileSize, int dstTileSize) {
        target.getChildren().clear();
        if (gids == null || gids.length == 0) return;
        int rows = gids.length;
        int cols = gids[0].length;
        int tilesetCols = Math.max(1, (int) Math.floor(tileset.getWidth() / srcTileSize));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int gid = gids[r][c];
                if (gid <= 0) continue; // 0 = empty
                int index = gid - 1;
                int sx = (index % tilesetCols) * srcTileSize;
                int sy = (index / tilesetCols) * srcTileSize;
                ImageView iv = new ImageView(tileset);
                iv.setViewport(new Rectangle2D(sx, sy, srcTileSize, srcTileSize));
                iv.setFitWidth(dstTileSize);
                iv.setFitHeight(dstTileSize);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);
                iv.setLayoutX(c * dstTileSize);
                iv.setLayoutY(r * dstTileSize);
                target.getChildren().add(iv);
            }
        }
    }
}


