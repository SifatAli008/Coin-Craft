package com.coincraft.game.tile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Deterministic ground tiling configuration (no randomness).
 */
public final class GroundConfig {
    public final int srcTileSize;
    public final int dstTileSize;
    public final int col;
    public final int row;
    public final String tilesetPath;

    public GroundConfig(int src, int dst, int col, int row, String path) {
        this.srcTileSize = src;
        this.dstTileSize = dst;
        this.col = col;
        this.row = row;
        this.tilesetPath = path;
    }

    public static GroundConfig load(String resourcePath) {
        try (InputStream is = GroundConfig.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            int src = extractInt(json, "srcTileSize", 16);
            int dst = extractInt(json, "dstTileSize", 48);
            int col = extractInt(json, "col", 1);
            int row = extractInt(json, "row", 2);
            String path = extractString(json, "tileset", "/Assets/Tilemap/plains.png");
            return new GroundConfig(src, dst, col, row, path);
        } catch (Exception e) {
            return null;
        }
    }

    private static int extractInt(String json, String key, int def) {
        int i = json.indexOf('"' + key + '"');
        if (i < 0) return def;
        int colon = json.indexOf(':', i);
        int comma = json.indexOf(',', colon);
        int end = comma > 0 ? comma : json.indexOf('}', colon);
        if (end < 0) end = json.length();
        String num = json.substring(colon + 1, end).trim().replaceAll("[^0-9]", "");
        if (num.isEmpty()) return def;
        return Integer.parseInt(num);
    }

    private static String extractString(String json, String key, String def) {
        int i = json.indexOf('"' + key + '"');
        if (i < 0) return def;
        int colon = json.indexOf(':', i);
        int q1 = json.indexOf('"', colon + 1);
        int q2 = json.indexOf('"', q1 + 1);
        if (q1 < 0 || q2 < 0) return def;
        return json.substring(q1 + 1, q2);
    }
}


