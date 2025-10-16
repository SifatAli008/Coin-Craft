package com.coincraft.game.tile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Lightweight config for autotile rules loaded from JSON resource.
 * { "srcTileSize":16, "dstTileSize":48, "originCol":4, "originRow":0, "tileset":"/Assets/Tilemap/plains.png" }
 */
public final class TilesetRulesConfig {
    public final int srcTileSize;
    public final int dstTileSize;
    public final int originCol;
    public final int originRow;
    public final String tilesetPath;
    public final boolean singleCell;

    public TilesetRulesConfig(int src, int dst, int col, int row, String path) {
        this.srcTileSize = src;
        this.dstTileSize = dst;
        this.originCol = col;
        this.originRow = row;
        this.tilesetPath = path;
        this.singleCell = false;
    }

    public static TilesetRulesConfig load(String resourcePath) {
        try (InputStream is = TilesetRulesConfig.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            int src = extractInt(json, "srcTileSize", 16);
            int dst = extractInt(json, "dstTileSize", 48);
            int col = extractInt(json, "originCol", 0);
            int row = extractInt(json, "originRow", 0);
            String path = extractString(json, "tileset", "/Assets/Tilemap/plains.png");
            boolean single = extractBool(json, "single", false);
            TilesetRulesConfig cfg = new TilesetRulesConfig(src, dst, col, row, path);
            // hack: set via reflection to keep constructor simple
            try {
                java.lang.reflect.Field f = TilesetRulesConfig.class.getDeclaredField("singleCell");
                f.setAccessible(true);
                f.setBoolean(cfg, single);
            } catch (Exception ignored) {}
            return cfg;
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
        String num = json.substring(colon + 1, end).trim();
        num = num.replaceAll("[^0-9]", "");
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

    private static boolean extractBool(String json, String key, boolean def) {
        int i = json.indexOf('"' + key + '"');
        if (i < 0) return def;
        int colon = json.indexOf(':', i);
        int end = json.indexOf(',', colon);
        if (end < 0) end = json.indexOf('}', colon);
        if (end < 0) end = json.length();
        String val = json.substring(colon + 1, end).trim().toLowerCase();
        if (val.startsWith("true")) return true;
        if (val.startsWith("false")) return false;
        return def;
    }
}


