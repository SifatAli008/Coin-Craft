package com.coincraft.tools;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Simple standalone manual map editor.
 * - Palette from a tileset image (plains.png)
 * - Paint canvas grid
 * - Save/Load CSV compatible with the in-game CSV loader
 */
public class MapEditorApp extends Application {
    private static final int SRC_TILE = 16;   // tileset cell size
    private static final int DST_TILE = 48;   // canvas cell size
    private static final int COLS = 26;       // default grid
    private static final int ROWS = 15;

    private int[][] gids = new int[ROWS][COLS];
    private int selectedGid = 1;

    @Override
    public void start(Stage stage) {
        Arrays.stream(gids).forEach(r -> Arrays.fill(r, 0));

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 800);

        // Load tileset
        Image tileset = new Image(getClass().getResourceAsStream("/Assets/Tilemap/plains.png"));
        int tilesetCols = Math.max(1, (int)Math.floor(tileset.getWidth() / SRC_TILE));
        int tilesetRows = Math.max(1, (int)Math.floor(tileset.getHeight() / SRC_TILE));

        // Palette grid
        ScrollPane paletteScroll = new ScrollPane();
        GridPane palette = new GridPane();
        palette.setHgap(4); palette.setVgap(4); palette.setPadding(new Insets(8));
        int gid = 1;
        for (int y = 0; y < tilesetRows; y++) {
            for (int x = 0; x < tilesetCols; x++) {
                ImageView iv = new ImageView(tileset);
                iv.setViewport(new Rectangle2D(x * SRC_TILE, y * SRC_TILE, SRC_TILE, SRC_TILE));
                iv.setFitWidth(32); iv.setFitHeight(32); iv.setSmooth(false); iv.setPreserveRatio(false);
                final int useGid = gid++;
                iv.setOnMouseClicked(e -> { selectedGid = useGid; });
                palette.add(iv, x, y);
            }
        }
        paletteScroll.setContent(palette);
        paletteScroll.setFitToWidth(true);
        paletteScroll.setPrefWidth(360);

        // Canvas grid
        Pane canvas = new Pane();
        canvas.setPrefSize(COLS * DST_TILE, ROWS * DST_TILE);
        canvas.setStyle("-fx-background-color: #a7f3d0; -fx-border-color: #0ea5e9;");
        canvas.setOnMouseDragged(e -> paintAt(canvas, tileset, (int)e.getX(), (int)e.getY(), e.getButton() != MouseButton.SECONDARY));
        canvas.setOnMousePressed(e -> paintAt(canvas, tileset, (int)e.getX(), (int)e.getY(), e.getButton() != MouseButton.SECONDARY));

        // Top bar
        Button btnNew = new Button("New");
        Button btnLoad = new Button("Load CSV");
        Button btnSave = new Button("Save CSV");
        Label status = new Label();
        HBox top = new HBox(10, btnNew, btnLoad, btnSave, status);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(8,12,8,12));

        btnNew.setOnAction(e -> { Arrays.stream(gids).forEach(r -> Arrays.fill(r, 0)); redraw(canvas, tileset); });
        btnSave.setOnAction(e -> {
            try {
                Path out = Path.of("edited/map.csv");
                Files.createDirectories(out.getParent());
                StringBuilder sb = new StringBuilder();
                for (int r = 0; r < ROWS; r++) {
                    for (int c = 0; c < COLS; c++) {
                        if (c > 0) sb.append(',');
                        sb.append(gids[r][c]);
                    }
                    sb.append('\n');
                }
                Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);
                status.setText("Saved to edited/map.csv");
            } catch (IOException ex) {
                status.setText("Save failed: " + ex.getMessage());
            }
        });
        btnLoad.setOnAction(e -> {
            try {
                Path in = Path.of("edited/map.csv");
                if (Files.exists(in)) {
                    java.util.List<String> lines = Files.readAllLines(in, StandardCharsets.UTF_8);
                    for (int r = 0; r < Math.min(ROWS, lines.size()); r++) {
                        String[] parts = lines.get(r).split(",");
                        for (int c = 0; c < Math.min(COLS, parts.length); c++) {
                            String p = parts[c].trim();
                            if (p.endsWith("\r")) p = p.substring(0, p.length()-1);
                            gids[r][c] = p.isEmpty() ? 0 : Integer.parseInt(p);
                        }
                    }
                    redraw(canvas, tileset);
                    status.setText("Loaded edited/map.csv");
                }
            } catch (Exception ex) {
                status.setText("Load failed: " + ex.getMessage());
            }
        });

        root.setTop(top);
        root.setLeft(paletteScroll);
        root.setCenter(new ScrollPane(canvas));

        stage.setTitle("CoinCraft Map Editor");
        stage.setScene(scene);
        stage.show();

        redraw(canvas, tileset);
    }

    private void paintAt(Pane canvas, Image tileset, int x, int y, boolean paint) {
        int c = x / DST_TILE;
        int r = y / DST_TILE;
        if (r < 0 || c < 0 || r >= ROWS || c >= COLS) return;
        gids[r][c] = paint ? selectedGid : 0;
        redraw(canvas, tileset);
    }

    private void redraw(Pane canvas, Image tileset) {
        canvas.getChildren().clear();
        int tilesetCols = Math.max(1, (int)Math.floor(tileset.getWidth() / SRC_TILE));
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int gid = gids[r][c];
                if (gid <= 0) continue;
                int idx = gid - 1;
                int sx = (idx % tilesetCols) * SRC_TILE;
                int sy = (idx / tilesetCols) * SRC_TILE;
                ImageView iv = new ImageView(tileset);
                iv.setViewport(new Rectangle2D(sx, sy, SRC_TILE, SRC_TILE));
                iv.setFitWidth(DST_TILE);
                iv.setFitHeight(DST_TILE);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);
                iv.setLayoutX(c * DST_TILE);
                iv.setLayoutY(r * DST_TILE);
                canvas.getChildren().add(iv);
            }
        }
        // grid lines
        for (int r = 0; r <= ROWS; r++) {
            javafx.scene.shape.Line h = new javafx.scene.shape.Line(0, r*DST_TILE, COLS*DST_TILE, r*DST_TILE);
            h.setStroke(javafx.scene.paint.Color.rgb(0,0,0,0.1));
            canvas.getChildren().add(h);
        }
        for (int c = 0; c <= COLS; c++) {
            javafx.scene.shape.Line v = new javafx.scene.shape.Line(c*DST_TILE, 0, c*DST_TILE, ROWS*DST_TILE);
            v.setStroke(javafx.scene.paint.Color.rgb(0,0,0,0.1));
            canvas.getChildren().add(v);
        }
    }

    public static void main(String[] args) { launch(args); }
}


