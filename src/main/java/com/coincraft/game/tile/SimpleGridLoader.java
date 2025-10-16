package com.coincraft.game.tile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Loads a boolean grid from a lightweight JSON resource with shape:
 * { "cols":N, "rows":M, "tiles":[0/1,... length M*N row-major] }
 */
public final class SimpleGridLoader {
    private SimpleGridLoader() {}

    public static boolean[][] loadGrid(String resourcePath) {
        try (InputStream is = SimpleGridLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            int cols = extractInt(json, "cols");
            int rows = extractInt(json, "rows");
            String tilesArr = extractArray(json, "tiles");
            String[] parts = tilesArr.split(",");
            if (parts.length < cols * rows) return null;
            boolean[][] grid = new boolean[rows][cols];
            int k = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    String t = parts[k++].trim();
                    if (t.endsWith("]")) t = t.substring(0, t.length()-1).trim();
                    grid[r][c] = t.equals("1");
                }
            }
            return grid;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean saveGrid(String filePath, boolean[][] grid) {
        try {
            int rows = grid.length;
            int cols = grid[0].length;
            StringBuilder sb = new StringBuilder();
            sb.append("{ \"cols\": ").append(cols).append(", \"rows\": ").append(rows).append(", \"tiles\": [\n");
            int k = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    sb.append(grid[r][c] ? '1' : '0');
                    k++;
                    if (k < rows * cols) sb.append(',');
                }
                sb.append('\n');
            }
            sb.append("]}\n");
            java.nio.file.Path p = java.nio.file.Paths.get(filePath);
            java.nio.file.Files.createDirectories(p.getParent());
            java.nio.file.Files.writeString(p, sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static int extractInt(String json, String key) {
        int i = json.indexOf('"' + key + '"');
        if (i < 0) return 0;
        int colon = json.indexOf(':', i);
        int comma = json.indexOf(',', colon);
        int end = comma > 0 ? comma : json.indexOf('}', colon);
        String num = json.substring(colon + 1, end).trim();
        num = num.replaceAll("[^0-9]", "");
        if (num.isEmpty()) return 0;
        return Integer.parseInt(num);
    }

    private static String extractArray(String json, String key) {
        int i = json.indexOf('"' + key + '"');
        if (i < 0) return "";
        int colon = json.indexOf(':', i);
        int lbr = json.indexOf('[', colon);
        int rbr = json.indexOf(']', lbr);
        if (lbr < 0 || rbr < 0) return "";
        return json.substring(lbr + 1, rbr);
    }
}


