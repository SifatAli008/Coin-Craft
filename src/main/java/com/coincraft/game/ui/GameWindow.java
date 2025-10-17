package com.coincraft.game.ui;

import com.coincraft.engine.GameLoop;
import com.coincraft.engine.Updatable;
import com.coincraft.engine.input.InputManager;
import com.coincraft.game.play.PlayerSheetController;
import com.coincraft.engine.rendering.Sprite;
import com.coincraft.game.play.SpriteSheetUtil;
import com.coincraft.game.play.TileCollisionMap;
import com.coincraft.game.play.CameraFollow;
import com.coincraft.game.models.GameLevel;
import com.coincraft.game.adventure.models.ConversationalNPCManager;
import com.coincraft.game.adventure.models.ConversationSystem;
import com.coincraft.game.adventure.models.NPCConversationTrees;
import com.coincraft.game.models.GameState;
import com.coincraft.game.models.Question;
import com.coincraft.game.models.QuestionChoice;
import com.coincraft.game.services.GameDataLoader;
import com.coincraft.game.services.GameProgressService;
import java.io.IOException;
import com.coincraft.models.User;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main game window for Financial Literacy Adventure
 */
public class GameWindow {
    private Stage stage;
    private VBox root;
    private final User currentUser;
    private final GameState gameState;
    private GameLevel currentLevel;
    private int currentQuestionIndex = 0;
    private int sessionCoins = 0;
    
    private final GameDataLoader dataLoader;
    private final GameProgressService progressService;
    
    // UI Components
    private Label coinLabel;
    private VBox contentArea;
    private GameLoop gameLoop;
    private boolean paused = false;
    
    public GameWindow(User user) {
        this.currentUser = user;
        this.dataLoader = GameDataLoader.getInstance();
        this.progressService = GameProgressService.getInstance();
        this.gameState = progressService.loadGameState(user.getUserId());
        
        initializeUI();
    }
    
    private void initializeUI() {
        stage = new Stage();
        stage.setTitle("Financial Literacy Adventure");
        
        root = new VBox(0);
        root.setStyle("-fx-background-color: #f8fafc;");
        
        // Top bar with coins and progress
        HBox topBar = createTopBar();
        
        // Main content area
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(20));
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        
        root.getChildren().addAll(topBar, contentArea);
        
        // Show level select screen
        showLevelSelect();
        
        Scene scene = new Scene(root, 1280, 720);
        // Setup pause toggle (ESC)
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> togglePause();
                default -> {}
            }
        });
        stage.setScene(scene);

        // Initialize a simple game loop updating minimal updatables
        gameLoop = new GameLoop();
        gameLoop.addUpdatable((Updatable) deltaTime -> {
            // Reserved for future per-frame UI/game updates in this window
        });

        // Start/stop lifecycle
        stage.setOnShown(e -> startLoop());
        stage.setOnHiding(e -> stopLoop());
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16));
        topBar.setStyle(
            "-fx-background-color: linear-gradient(to right, #667EEA, #764BA2);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
        );
        
        // Adventurer avatar
        StackPane avatarContainer = AdventurerAvatar.createCircularAvatar(48);
        
        // Game title with adventurer name
        VBox titleBox = new VBox(2);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("💰 Financial Adventure");
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label adventurerLabel = new Label("⚔️ " + currentUser.getName() + " the Knight");
        adventurerLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        titleBox.getChildren().addAll(titleLabel, adventurerLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        coinLabel = new Label("💰 " + currentUser.getSmartCoinBalance() + " Coins");
        coinLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 20;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        topBar.getChildren().addAll(avatarContainer, titleBox, spacer, coinLabel);
        return topBar;
    }
    
    private void showLevelSelect() {
        contentArea.getChildren().clear();
        
        Label selectLabel = new Label("Choose Your Adventure");
        selectLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        VBox levelsContainer = new VBox(16);
        levelsContainer.setAlignment(Pos.CENTER);
        levelsContainer.setMaxWidth(600);
        
        // Get total levels
        int totalLevels = dataLoader.getTotalLevels();
        
        for (int i = 1; i <= totalLevels; i++) {
            final int levelId = i;
            boolean isCompleted = gameState.isLevelCompleted(levelId);
            boolean isLocked = levelId > 1 && !gameState.isLevelCompleted(levelId - 1);
            
            HBox levelCard = createLevelCard(levelId, isCompleted, isLocked);
            levelsContainer.getChildren().add(levelCard);
        }
        
        // Free Play button to try movement prototype
        Button freePlay = new Button("Free Play (Prototype)");
        freePlay.setStyle(
            "-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: 700; -fx-background-radius: 8; -fx-padding: 8 16;"
        );
        freePlay.setOnAction(e -> startFreePlay());

        contentArea.getChildren().addAll(selectLabel, levelsContainer, freePlay);
    }
    
    private HBox createLevelCard(int levelId, boolean isCompleted, boolean isLocked) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefWidth(550);
        card.setStyle(
            "-fx-background-color: " + (isLocked ? "#e2e8f0" : "white") + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + (isCompleted ? "#10B981" : "#cbd5e1") + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        // Load level data for title
        GameLevel level = dataLoader.loadLevel(levelId);
        String levelTitle = level != null ? level.getTitle() : "Level " + levelId;
        
        // Create NPC preview (smaller version)
        javafx.scene.Node npcPreview = createNPCPreview(level);

        
        VBox infoBox = new VBox(4);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(levelTitle);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (isLocked ? "#94a3b8" : "#1e293b") + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label statusLabel = new Label(
            isCompleted ? "✅ Completed" : 
            isLocked ? "🔒 Locked" : 
            "▶️ Start"
        );
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + (isLocked ? "#94a3b8" : "#64748b") + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        infoBox.getChildren().addAll(titleLabel, statusLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button playButton = new Button(isCompleted ? "Replay" : "Play");
        playButton.setDisable(isLocked);
        playButton.setPrefWidth(100);
        playButton.setPrefHeight(40);
        playButton.setStyle(
            "-fx-background-color: " + (isLocked ? "#cbd5e1" : "#FA8A00") + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: " + (isLocked ? "not-allowed" : "hand") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        if (!isLocked) {
            playButton.setOnAction(e -> startLevel(levelId));
            
            playButton.setOnMouseEntered(e -> {
                playButton.setStyle(
                    "-fx-background-color: #E67E00;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
                );
            });
            
            playButton.setOnMouseExited(e -> {
                playButton.setStyle(
                    "-fx-background-color: #FA8A00;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
                );
            });
        }
        
        card.getChildren().addAll(npcPreview, infoBox, spacer, playButton);
        return card;
    }
    
    /**
     * Create smaller NPC preview for level select cards
     */
    private javafx.scene.Node createNPCPreview(GameLevel level) {
        if (level == null) {
            Label fallback = new Label("🎮");
            fallback.setStyle("-fx-font-size: 48px;");
            return fallback;
        }
        
        String imagePath = level.getNpcImagePath();
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.Image npcImage = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(imagePath)
                );
                
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(npcImage);
                imageView.setFitWidth(64);
                imageView.setFitHeight(64);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                
                return imageView;
                
            } catch (Exception e) {
                // Fallback to emoji
            }
        }
        
        // Emoji fallback
        Label emojiLabel = new Label(level.getNpcEmoji());
        emojiLabel.setStyle("-fx-font-size: 48px;");
        return emojiLabel;
    }
    
    private void startLevel(int levelId) {
        currentLevel = dataLoader.loadLevel(levelId);
        if (currentLevel == null) {
            System.err.println("Could not load level " + levelId);
            return;
        }
        
        currentQuestionIndex = 0;
        sessionCoins = 0;
        showNPCIntro();
    }

    private void startFreePlay() {
        contentArea.getChildren().clear();

        // Simple playfield (viewport container)
        double viewportW = 1280;
        double viewportH = 720;
        StackPane playfield = new StackPane();
        playfield.setStyle("-fx-background-color: linear-gradient(to bottom, #dbeafe, #bfdbfe);");
        playfield.setPrefSize(viewportW, viewportH);
        playfield.setMinSize(viewportW, viewportH);
        playfield.setMaxSize(viewportW, viewportH);

        // Prototype player using sprite sheet assets (idle/walk)
        javafx.scene.image.ImageView player = new javafx.scene.image.ImageView();
        javafx.scene.image.Image idleImg;
        javafx.scene.image.Image walkImg;
        try {
            idleImg = new javafx.scene.image.Image(
                getClass().getResourceAsStream(
                    "/Assets/Sprites/Player/Side animations/spr_player_right_idle.png"
                )
            );
        } catch (Exception ex) {
            idleImg = null;
        }
        try {
            walkImg = new javafx.scene.image.Image(
                getClass().getResourceAsStream(
                    "/Assets/Sprites/Player/Side animations/spr_player_right_walk.png"
                )
            );
        } catch (Exception ex) {
            walkImg = null;
        }
        if (idleImg != null) {
            player.setImage(idleImg);
        }
        player.setFitWidth(64);
        player.setFitHeight(64);
        player.setPreserveRatio(true);
        player.setSmooth(true);

        // World pane is larger than viewport for scrolling
        double worldW = Math.max(1800, viewportW * 2); // scale with viewport
        double worldH = Math.max(1160, viewportH * 2);
        javafx.scene.layout.Pane pane = new javafx.scene.layout.Pane();
        pane.setPrefSize(worldW, worldH);
        // Separate layers for clean re-rendering of paths and decor
        javafx.scene.layout.Pane baseLayer = new javafx.scene.layout.Pane();
        javafx.scene.layout.Pane decorLayer = new javafx.scene.layout.Pane();
        javafx.scene.layout.Pane pathLayer = new javafx.scene.layout.Pane();
        baseLayer.setPrefSize(worldW, worldH);
        decorLayer.setPrefSize(worldW, worldH);
        pathLayer.setPrefSize(worldW, worldH);
        pane.getChildren().addAll(baseLayer, decorLayer, pathLayer);
        // Clip the viewport (not the world) so camera translations don't move the clip
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(viewportW, viewportH);
        clip.widthProperty().bind(playfield.widthProperty());
        clip.heightProperty().bind(playfield.heightProperty());
        playfield.setClip(clip);

        // No HUD - clean game screen
        
        // No editor toolbar - clean game screen

        // Render tilemap background (plains)
        TileCollisionMap collisionMap = null;
        try {
            // Scale tiles up to match 96x96 player (use 48x48 tiles)
            final int tileSize = 48;
            final int srcTileSize = 16;
            int colsForTiles = (int) (worldW / tileSize);
            int rowsForTiles = (int) (worldH / tileSize);
            // Ground: repeat grass.png everywhere
            Image grassImg = new Image(getClass().getResourceAsStream("/Assets/Tilemap/grass.png"));
            for (int r = 0; r < rowsForTiles; r++) {
                for (int c = 0; c < colsForTiles; c++) {
                    ImageView iv = new ImageView(grassImg);
                    iv.setFitWidth(tileSize);
                    iv.setFitHeight(tileSize);
                    iv.setPreserveRatio(false);
                    iv.setSmooth(true);
                    iv.setLayoutX(c * tileSize);
                    iv.setLayoutY(r * tileSize);
                    baseLayer.getChildren().add(iv);
                }
            }
            // Decor: prefer external CSV (manual placement); else scatter random
            Image decorImg = new Image(getClass().getResourceAsStream("/Assets/Tilemap/decor_16x16.png"));
            boolean renderedDecorFromCsv = false;
            try {
                java.nio.file.Path dcsv = java.nio.file.Paths.get("edited/decor.csv");
                if (java.nio.file.Files.exists(dcsv)) {
                    int[][] gids = com.coincraft.game.tile.TiledCsvMapLoader.loadCsv(dcsv);
                    com.coincraft.game.tile.TiledCsvMapLoader.renderLayer(decorLayer, decorImg, gids, srcTileSize, tileSize);
                    renderedDecorFromCsv = true;
                }
            } catch (IOException ignored) {}
            if (!renderedDecorFromCsv) {
                java.util.Random rng = new java.util.Random(1234);
                int decorCols = Math.max(1, (int)Math.floor(decorImg.getWidth() / srcTileSize));
                int decorRows = Math.max(1, (int)Math.floor(decorImg.getHeight() / srcTileSize));
                for (int r = 1; r < rowsForTiles - 1; r++) {
                    for (int c = 1; c < colsForTiles - 1; c++) {
                        if (rng.nextDouble() < 0.08) { // 8% chance
                            int idx = rng.nextInt(decorCols * decorRows);
                            int sx = (idx % decorCols) * srcTileSize;
                            int sy = (idx / decorCols) * srcTileSize;
                            ImageView iv = new ImageView(decorImg);
                            iv.setViewport(new javafx.geometry.Rectangle2D(sx, sy, srcTileSize, srcTileSize));
                            iv.setFitWidth(tileSize);
                            iv.setFitHeight(tileSize);
                            iv.setPreserveRatio(false);
                            iv.setSmooth(true);
                            iv.setLayoutX(c * tileSize);
                            iv.setLayoutY(r * tileSize);
                            decorLayer.getChildren().add(iv);
                        }
                    }
                }
            }

            // External CSV import disabled for color-only mode

            // Autotiled path layer (simple horizontal + vertical cross)
            int cols = (int) (worldW / tileSize);
            int rows = (int) (worldH / tileSize);
            // Prefer edited path file if present, else fallback to bundled resource
            boolean[][] path = null;
            try {
                java.nio.file.Path editedPath = java.nio.file.Paths.get("edited/free_play_path.json");
                if (java.nio.file.Files.exists(editedPath)) {
                    String json = java.nio.file.Files.readString(editedPath);
                    // Load from string via temporary buffer
                    java.nio.file.Path tmp = java.nio.file.Files.createTempFile("path", ".json");
                    java.nio.file.Files.writeString(tmp, json);
                    path = com.coincraft.game.tile.SimpleGridLoader.loadGrid(tmp.toUri().toURL().toString());
                    java.nio.file.Files.deleteIfExists(tmp);
                }
            } catch (java.io.IOException ignored) {}
            if (path == null) {
                path = com.coincraft.game.tile.SimpleGridLoader.loadGrid("/game/levels/free_play_path.json");
            }
            if (path == null || path.length != rows || path[0].length != cols) {
                path = new boolean[rows][cols];
                int midR = rows / 2;
                int midC = cols / 2;
                for (int c = 2; c < cols - 2; c++) path[midR][c] = true;
                for (int r = 2; r < rows - 2; r++) path[r][midC] = true;
            }

            // Load rules config so art tweaks don't require code edits
            // Prefer edited rules file if present, else fallback to bundled resource
            com.coincraft.game.tile.TilesetRulesConfig rulesCfg = null;
            try {
                java.nio.file.Path editedRules = java.nio.file.Paths.get("edited/free_play_rules.json");
                if (java.nio.file.Files.exists(editedRules)) {
                    String json = java.nio.file.Files.readString(editedRules);
                    java.nio.file.Path tmp = java.nio.file.Files.createTempFile("rules", ".json");
                    java.nio.file.Files.writeString(tmp, json);
                    rulesCfg = com.coincraft.game.tile.TilesetRulesConfig.load(tmp.toUri().toURL().toString());
                    java.nio.file.Files.deleteIfExists(tmp);
                }
            } catch (java.io.IOException ignored) {}
            if (rulesCfg == null) rulesCfg = com.coincraft.game.tile.TilesetRulesConfig.load("/game/levels/free_play_rules.json");
            final com.coincraft.game.tile.TilesetRulesConfig rulesCfgFinal = (rulesCfg != null)
                ? rulesCfg
                : new com.coincraft.game.tile.TilesetRulesConfig(16, tileSize, 4, 0, "/Assets/Tilemap/plains.png");
            // Color mode: skip creating unused rules
            // Render path as solid colored tiles (kept for clarity)
            com.coincraft.game.tile.ColorTileRenderer colorPath = new com.coincraft.game.tile.ColorTileRenderer(pathLayer, rulesCfgFinal.dstTileSize);
            colorPath.renderBooleanGrid(path, javafx.scene.paint.Color.web("#f59e0b"));
            // Simple in-scene path brush with UI controls; keyboard still works (Shift/Alt)
            // No mouse handlers - clean game screen
            // No keyboard save handlers - clean game screen

            // No rule origin HUD - clean game screen

            // No keyboard handlers - clean game screen
            // Build simple border collision (solid outer ring)
            collisionMap = new TileCollisionMap(cols, rows, tileSize);
            for (int c = 0; c < cols; c++) {
                collisionMap.setBlocked(c, 0, true);
                collisionMap.setBlocked(c, rows - 1, true);
            }
            for (int r = 0; r < rows; r++) {
                collisionMap.setBlocked(0, r, true);
                collisionMap.setBlocked(cols - 1, r, true);
            }

            // Reserve spawn area around center so player doesn't spawn blocked
            int spawnCol = cols / 2;
            int spawnRow = rows / 2;
            int reserveRadius = 2; // keep 5x5 clear
            java.util.function.BiPredicate<Integer,Integer> isReserved = (gc, gr) ->
                Math.abs(gc - spawnCol) <= reserveRadius && Math.abs(gr - spawnRow) <= reserveRadius;

            // Place rock sprites as collidable obstacles (cannot walk through)
            try {
                Image[] rocks = new Image[] {
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_boulder1.png")),
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_boulder2.png")),
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_boulder3.png")),
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_smallrock2.png")),
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_boulder4.png")),
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Rocks/spr_smallrock1.png"))
                };
                java.util.Random rockRng = new java.util.Random(4321);
                for (int ry = 1; ry < rows - 1; ry++) {
                    for (int cx = 1; cx < cols - 1; cx++) {
                        if (path[ry][cx]) continue; // keep paths clear
                        if (isReserved.test(cx, ry)) continue; // keep spawn clear
                        if (rockRng.nextDouble() < 0.05) { // 5% chance per cell
                            Image rock = rocks[rockRng.nextInt(rocks.length)];
                            addObstacle(rock, decorLayer, cx, ry, 1, 1, tileSize, collisionMap);
                        }
                    }
                }
            } catch (Exception ignoredRocks) {}

            // Vegetation (bushes/trees) as collidable obstacles
            try {
                Image[] veg = new Image[] {
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/Bush.png")),      // 1x1
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/Bush1.png")),     // 1x1
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/spr_tree1.png")), // 2x2
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/spr_tree2.png")), // 2x2
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/spr_tree3.png")), // 2x2
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/tree.png")),      // 2x2
                    new Image(getClass().getResourceAsStream("/Assets/Sprites/Vegetation/tree1.png"))      // 2x2
                };
                java.util.Random vegRng = new java.util.Random(9876);
                for (int ry = 1; ry < rows - 1; ry++) {
                    for (int cx = 1; cx < cols - 1; cx++) {
                        if (path[ry][cx]) continue; // keep paths clear
                        if (isReserved.test(cx, ry)) continue; // keep spawn clear
                        if (vegetationAllowedOnTile(vegRng)) {
                            int idx = vegRng.nextInt(veg.length);
                            Image im = veg[idx];
                            int wTiles = (idx <= 1) ? 1 : 2; // bushes 1x1, trees 2x2
                            int hTiles = (idx <= 1) ? 1 : 2;
                            // ensure footprint is within bounds and not on path/blocked
                            boolean ok = true;
                            for (int rr = 0; rr < hTiles && ok; rr++) {
                                for (int cc = 0; cc < wTiles; cc++) {
                                    int gr = ry + rr, gc = cx + cc;
                                    if (gr >= rows - 1 || gc >= cols - 1 || path[gr][gc] || isReserved.test(gc, gr) || collisionMap.isBlockedTile(gc, gr)) { ok = false; break; }
                                }
                            }
                            if (ok) {
                                addObstacle(im, decorLayer, cx, ry, wTiles, hTiles, tileSize, collisionMap);
                            }
                        }
                    }
                }
            } catch (Exception ignoredVegetation) {}

            // Collision map only; decor drawn above already
        } catch (Exception ignore) {}

        pane.getChildren().add(player);
        // Spawn the player centered on the reserved middle tile so they never start blocked
        final int spawnTileSize = (collisionMap != null) ? collisionMap.getTileSize() : 48;
        final int spawnColForPlace = (int)Math.floor((worldW / spawnTileSize) / 2.0);
        final int spawnRowForPlace = (int)Math.floor((worldH / spawnTileSize) / 2.0);
        final double playerW = Math.max(1.0, player.getBoundsInParent().getWidth());
        final double playerH = Math.max(1.0, player.getBoundsInParent().getHeight());
        final double spawnX = spawnColForPlace * spawnTileSize + (spawnTileSize * 0.5) - (playerW * 0.5);
        final double spawnY = spawnRowForPlace * spawnTileSize + (spawnTileSize * 0.5) - (playerH * 0.5);
        player.setLayoutX(spawnX);
        player.setLayoutY(spawnY);
        // Center the viewport initially on the player
        pane.setTranslateX(-(player.getLayoutX() - viewportW / 2));
        pane.setTranslateY(-(player.getLayoutY() - viewportH / 2));

        // Compose scene
        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);

        // Add world inside the fixed-size playfield to avoid layout overflow
        playfield.getChildren().add(pane);
        container.getChildren().add(playfield);
        contentArea.getChildren().add(container);

        // Hook input manager from the window's scene
        InputManager inputManager = new InputManager(stage.getScene());

        // Build sprites from sheets (assumes square frames in a row)
        final Sprite idleSprite;
        final Sprite walkSprite;
        Sprite tmpIdle = null;
        Sprite tmpWalk = null;
        if (idleImg != null) tmpIdle = SpriteSheetUtil.createSquareRowSprite(idleImg);
        if (walkImg != null) tmpWalk = SpriteSheetUtil.createSquareRowSprite(walkImg);
        if (tmpIdle == null) return; // require idle
        if (tmpWalk == null) tmpWalk = tmpIdle; // fallback
        idleSprite = tmpIdle;
        walkSprite = tmpWalk;
        // Increase player size
        idleSprite.setSize(128, 128);
        walkSprite.setSize(128, 128);

        // Position center
        idleSprite.setPosition(worldW / 2 - 32, worldH / 2 - 32);
        walkSprite.setPosition(worldW / 2 - 32, worldH / 2 - 32);

        // Render into pane
        pane.getChildren().add(idleSprite.getNode());
        pane.getChildren().add(walkSprite.getNode());
        walkSprite.getNode().setVisible(false);

        PlayerSheetController controller = new PlayerSheetController(
            inputManager,
            idleSprite,
            walkSprite,
            220,
            0, 0, worldW, worldH,
            collisionMap
        );

        // Register controller with the game loop
        gameLoop.addUpdatable(controller);

        // Camera follow on idle sprite (active sprite position is kept in sync)
        CameraFollow camera = new CameraFollow(
            pane,
            idleSprite,
            viewportW,
            viewportH,
            worldW,
            worldH
        );
        gameLoop.addUpdatable(camera);

        // Add NPCs to the game world
        addNPCsToGameWorld(pane, worldW, worldH);
        
        // Initialize CoinManager with current user
        com.coincraft.game.adventure.models.CoinManager.initialize(currentUser);
        
        // Add NPC interaction system
        addNPCInteractionSystem(controller, pane, idleSprite, inputManager);
        
        // Add NPC collision detection
        addNPCCollisionDetection(controller, pane, idleSprite, inputManager);
        
    }
    
    /**
     * Add NPCs to the Free Play game world
     */
    private void addNPCsToGameWorld(javafx.scene.layout.Pane gameWorld, double worldW, double worldH) {
        System.out.println("🎭 Adding Conversational NPCs to Free Play game world");
        
        // Create Conversational NPC Manager
        ConversationalNPCManager npcManager = new ConversationalNPCManager();
        
        // Position NPCs in walkable areas
        double adventurerX = worldW * 0.2;  // Left side
        double adventurerY = worldH * 0.3;  // Top area
        
        double wiseLadyX = worldW * 0.5;   // Center horizontally
        double wiseLadyY = worldH * 0.3;   // Top area
        
        double businessmanX = worldW * 0.8;  // Right side
        double businessmanY = worldH * 0.3;   // Top area
        
        // Create Strong Adventurer with full conversation system
        npcManager.createAdventurer(
            "Strong Adventurer",
            adventurerX, adventurerY
        );
        
        // Create Wise Lady with comprehensive financial education
        npcManager.createSage(
            "Wise Lady",
            wiseLadyX, wiseLadyY
        );
        
        // Create Smart Businessman with quiz system
        npcManager.createMerchant(
            "Smart Businessman",
            businessmanX, businessmanY
        );
        
        // Render all NPCs in the game world
        npcManager.renderAll(gameWorld);
        
        System.out.println("✅ Added " + npcManager.getNPCCount() + " Conversational NPCs to Free Play world");
        System.out.println("🎮 NPCs now have full conversation systems!");
        System.out.println("💡 Walk near NPCs and press SPACE to start conversations");
        System.out.println("🗣️ Each NPC has multiple conversation topics and interactive dialogue trees");
    }
    
    /**
     * Add NPC interaction system to handle conversations
     */
    private void addNPCInteractionSystem(PlayerSheetController controller, javafx.scene.layout.Pane gameWorld, Sprite idleSprite, InputManager inputManager) {
        // Note: controller parameter kept for future use
        System.out.println("💬 Adding NPC interaction system to Free Play");
        
        // Track space key state for edge detection
        final boolean[] prevSpacePressed = {false};
        
        // Create a custom updatable for NPC interactions
        gameLoop.addUpdatable(deltaTime -> {
            // Check for SPACE key press with edge detection
            boolean spacePressed = inputManager.isKeyPressed(javafx.scene.input.KeyCode.SPACE);
            
            // Only trigger on key press, not while held
            if (spacePressed && !prevSpacePressed[0]) {
                System.out.println("🔘 SPACE pressed - checking for NPC interactions");
                
                // Get player position
                double playerX = idleSprite.getX();
                double playerY = idleSprite.getY();
                
                System.out.println("Player position: (" + playerX + ", " + playerY + ")");
                
                // Check for nearby NPCs and interact
                checkForNPCInteractions(gameWorld, playerX, playerY);
            }
            
            // Update space key state
            prevSpacePressed[0] = spacePressed;
        });
        
        System.out.println("✅ NPC interaction system added - Press SPACE near NPCs to start conversations");
    }
    
    /**
     * Check for NPCs near the player and handle interactions
     */
    private void checkForNPCInteractions(javafx.scene.layout.Pane gameWorld, double playerX, double playerY) {
        System.out.println("🔍 Checking for NPC interactions...");
        
        boolean foundNPC = false;
        
        // Look for NPCs in the game world
        for (javafx.scene.Node node : getAllDescendants(gameWorld)) {
            if (node instanceof javafx.scene.shape.Circle npcCircle) {
                double npcX = npcCircle.getLayoutX();
                double npcY = npcCircle.getLayoutY();
                double distance = Math.sqrt(Math.pow(playerX - npcX, 2) + Math.pow(playerY - npcY, 2));
                
                if (distance <= 200) {
                    System.out.println("✅ NPC within interaction range!");
                    startConversationWithNPC(npcCircle);
                    foundNPC = true;
                    break;
                }
            } else if (node instanceof javafx.scene.image.ImageView npcImage) {
                double npcX = npcImage.getLayoutX() + Math.max(1.0, npcImage.getBoundsInParent().getWidth()) * 0.5;
                double npcY = npcImage.getLayoutY() + Math.max(1.0, npcImage.getBoundsInParent().getHeight()) * 0.5;
                double distance = Math.sqrt(Math.pow(playerX - npcX, 2) + Math.pow(playerY - npcY, 2));
                
                if (distance <= 200) {
                    String npcNameNearby = null;
                    for (javafx.scene.Node sibling : getAllDescendants(gameWorld)) {
                        if (sibling instanceof javafx.scene.text.Text t) {
                            double tx = t.getLayoutX();
                            double ty = t.getLayoutY();
                            double nameDist = Math.sqrt(Math.pow(tx - npcX, 2) + Math.pow(ty - (npcY - 40), 2));
                            if (nameDist < 120) { 
                                npcNameNearby = t.getText(); 
                                break; 
                            }
                        }
                    }
                    
                    String npcType = "unknown";
                    if (npcNameNearby != null) {
                        String s = npcNameNearby.toLowerCase();
                        if (s.contains("adventurer")) npcType = "adventurer";
                        else if (s.contains("wise")) npcType = "wise_lady";
                        else if (s.contains("business")) npcType = "businessman";
                    }
                    
                    System.out.println("✅ Image NPC within range. Name: " + npcNameNearby + ", Type: " + npcType);
                    startConversationWithNPC(npcImage, npcNameNearby, npcType);
                    foundNPC = true;
                    break;
                }
            }
        }
        
        if (!foundNPC) {
            System.out.println("❌ No NPCs found within interaction range");
        }
    }
    
    /**
     * Collect all descendant nodes (breadth-first) under the given root pane
     */
    private java.util.List<javafx.scene.Node> getAllDescendants(javafx.scene.Parent root) {
        java.util.ArrayList<javafx.scene.Node> all = new java.util.ArrayList<>();
        java.util.ArrayDeque<javafx.scene.Parent> q = new java.util.ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            javafx.scene.Parent p = q.poll();
            for (javafx.scene.Node n : p.getChildrenUnmodifiable()) {
                all.add(n);
                if (n instanceof javafx.scene.Parent child) {
                    q.add(child);
                }
            }
        }
        return all;
    }
    
    /**
     * Start conversation with an NPC (Circle fallback)
     */
    private void startConversationWithNPC(javafx.scene.shape.Circle npcCircle) {
        System.out.println("💬 Starting conversation with Circle NPC at position: (" + npcCircle.getLayoutX() + ", " + npcCircle.getLayoutY() + ")");
        // For now, show a simple interaction message
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("NPC Interaction");
        alert.setHeaderText("NPC Conversation");
        alert.setContentText("You've found an NPC! The conversation system is now active. This NPC would normally start a full dialogue tree with multiple conversation options.");
        alert.showAndWait();
    }
    
    /**
     * Start conversation with an NPC (ImageView with name and type)
     */
    private void startConversationWithNPC(javafx.scene.image.ImageView npcImage, String npcName, String npcType) {
        System.out.println("💬 Starting conversation with " + npcName + " (Type: " + npcType + ") at position: (" + npcImage.getLayoutX() + ", " + npcImage.getLayoutY() + ")");
        
        // Create conversation system based on NPC type
        ConversationSystem.DialogueNode conversationTree;
        String npcDisplayType;
        
        switch (npcType) {
            case "adventurer" -> {
                conversationTree = NPCConversationTrees.createAdventurerConversation();
                npcDisplayType = "Game Guide";
            }
            case "wise_lady" -> {
                conversationTree = NPCConversationTrees.createWiseLadyConversation();
                npcDisplayType = "Financial Sage";
            }
            case "businessman" -> {
                conversationTree = NPCConversationTrees.createBusinessmanConversation();
                npcDisplayType = "Business Expert";
            }
            default -> {
                System.out.println("❌ Unknown NPC type: " + npcType);
                return;
            }
        }
        
        if (conversationTree != null) {
            ConversationSystem conversationSystem = new ConversationSystem(npcName, npcDisplayType, conversationTree);
            conversationSystem.startConversation();
        }
    }
    
    /**
     * Add NPC collision detection with reduced collision area and no push-back
     */
    private void addNPCCollisionDetection(PlayerSheetController controller, javafx.scene.layout.Pane gameWorld, Sprite idleSprite, InputManager inputManager) {
        // Note: controller and inputManager parameters kept for future use
        System.out.println("🛡️ Adding NPC collision detection to Free Play");
        
        // Create a custom updatable for NPC collision detection
        gameLoop.addUpdatable(deltaTime -> {
            // Get player position and size
            double playerX = idleSprite.getX();
            double playerY = idleSprite.getY();
            double playerWidth = Math.max(1.0, idleSprite.getWidth());
            double playerHeight = Math.max(1.0, idleSprite.getHeight());
            double playerRadius = Math.max(playerWidth, playerHeight) * 0.5;
            
            // Check collision with all NPCs
            for (javafx.scene.Node node : getAllDescendants(gameWorld)) {
                // Skip player's own sprite
                if (node == idleSprite.getNode()) continue;
                
                // Only consider likely NPC visuals
                if (!isLikelyNPC(node, gameWorld)) continue;
                
                javafx.geometry.Point2D npcCenter;
                double npcRadius;
                
                if (node instanceof javafx.scene.shape.Circle c) {
                    npcCenter = new javafx.geometry.Point2D(c.getLayoutX(), c.getLayoutY());
                    npcRadius = Math.max(1.0, c.getRadius());
                } else if (node instanceof javafx.scene.image.ImageView iv) {
                    npcCenter = new javafx.geometry.Point2D(
                        iv.getLayoutX() + Math.max(1.0, iv.getBoundsInParent().getWidth()) * 0.5,
                        iv.getLayoutY() + Math.max(1.0, iv.getBoundsInParent().getHeight()) * 0.5
                    );
                    npcRadius = Math.max(iv.getBoundsInParent().getWidth(), iv.getBoundsInParent().getHeight()) * 0.5;
                } else {
                    continue;
                }
                
                // Calculate distance between player and NPC
                double dx = playerX - npcCenter.getX();
                double dy = playerY - npcCenter.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                double minDistance = npcRadius + playerRadius + 2.0; // Reduced to 2 pixel buffer
                
                // If too close, just log the collision (no push-back)
                if (distance > 0 && distance < minDistance) {
                    System.out.println("🚫 NPC collision detected: player too close to NPC (distance: " + String.format("%.1f", distance) + "px)");
                    break; // Only handle one collision per frame
                }
            }
        });
        
        System.out.println("✅ NPC collision detection added - Reduced collision area (2px buffer, no push-back)");
    }
    
    /**
     * Check if a node is likely an NPC visual
     */
    private boolean isLikelyNPC(javafx.scene.Node node, javafx.scene.Parent searchRoot) {
        if (node instanceof javafx.scene.shape.Circle) return true;
        if (node instanceof javafx.scene.image.ImageView iv) {
            // Check for nearby name text within ~140px above the image center
            javafx.geometry.Bounds b = iv.localToScene(iv.getBoundsInLocal());
            javafx.geometry.Point2D centerScene = new javafx.geometry.Point2D(
                (b.getMinX() + b.getMaxX()) * 0.5,
                (b.getMinY() + b.getMaxY()) * 0.5
            );
            javafx.geometry.Point2D centerLocal = searchRoot.sceneToLocal(centerScene);
            for (javafx.scene.Node sibling : getAllDescendants(searchRoot)) {
                if (sibling instanceof javafx.scene.text.Text t) {
                    double dx = t.getLayoutX() - centerLocal.getX();
                    double dy = (t.getLayoutY() - 40) - centerLocal.getY();
                    if (Math.hypot(dx, dy) < 140) return true;
                }
            }
        }
        return false;
    }
    
    private void showNPCIntro() {
        contentArea.getChildren().clear();
        
        // Background with level style
        contentArea.setStyle("-fx-background: " + currentLevel.getBackgroundStyle() + ";");
        
        VBox introBox = new VBox(30);
        introBox.setAlignment(Pos.CENTER);
        introBox.setMaxWidth(700);
        
        // NPC character sprite or emoji
        javafx.scene.Node npcDisplay = createNPCDisplay();
        
        // NPC name
        Label npcName = new Label(currentLevel.getNpcName());
        npcName.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        // Dialog box
        VBox dialogBox = new VBox(16);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(30));
        dialogBox.setMaxWidth(600);
        dialogBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, 8);"
        );
        
        Label greetingText = new Label(currentLevel.getNpcGreeting());
        greetingText.setWrapText(true);
        greetingText.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        Button startButton = new Button("Let's Begin!");
        startButton.setPrefWidth(200);
        startButton.setPrefHeight(50);
        startButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        startButton.setOnAction(e -> showQuestion());
        
        dialogBox.getChildren().addAll(greetingText, startButton);
        introBox.getChildren().addAll(npcDisplay, npcName, dialogBox);
        
        contentArea.getChildren().add(introBox);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(500), introBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private void showQuestion() {
        if (currentQuestionIndex >= currentLevel.getQuestions().size()) {
            completeLevel();
            return;
        }
        
        contentArea.getChildren().clear();
        
        Question question = currentLevel.getQuestions().get(currentQuestionIndex);
        
        VBox questionBox = new VBox(30);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setMaxWidth(700);
        questionBox.setPadding(new Insets(40));
        
        // Question number
        Label questionNum = new Label("Question " + (currentQuestionIndex + 1) + " of " + currentLevel.getQuestions().size());
        questionNum.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Question card
        VBox questionCard = new VBox(20);
        questionCard.setAlignment(Pos.CENTER);
        questionCard.setPadding(new Insets(30));
        questionCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, 8);"
        );
        
        Label questionText = new Label(question.getText());
        questionText.setWrapText(true);
        questionText.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        VBox choicesBox = new VBox(12);
        choicesBox.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < question.getChoices().size(); i++) {
            QuestionChoice choice = question.getChoices().get(i);
            Button choiceButton = createChoiceButton(choice, question);
            choicesBox.getChildren().add(choiceButton);
        }
        
        questionCard.getChildren().addAll(questionText, choicesBox);
        questionBox.getChildren().addAll(questionNum, questionCard);
        
        contentArea.getChildren().add(questionBox);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(400), questionBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private Button createChoiceButton(QuestionChoice choice, Question question) {
        Button button = new Button(choice.getText());
        button.setPrefWidth(550);
        button.setPrefHeight(60);
        button.setWrapText(true);
        button.setStyle(
            "-fx-background-color: #f1f5f9;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #cbd5e1;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: #e0e7ff;" +
                "-fx-text-fill: #1e293b;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #667EEA;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: #f1f5f9;" +
                "-fx-text-fill: #1e293b;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #cbd5e1;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
        });
        
        button.setOnAction(e -> handleAnswer(choice, question));
        
        return button;
    }
    
    private void handleAnswer(QuestionChoice choice, Question question) {
        boolean isCorrect = choice.isCorrect();
        int coinChange = isCorrect ? choice.getReward() : choice.getPenalty();
        
        // Update coins
        sessionCoins += coinChange;
        currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + coinChange);
        updateCoinDisplay();
        
        // Record answer
        gameState.recordAnswer(isCorrect);
        
        // Show result
        showAnswerResult(isCorrect, coinChange, question.getExplanation());
    }
    
    private void showAnswerResult(boolean isCorrect, int coinChange, String explanation) {
        contentArea.getChildren().clear();
        
        VBox resultBox = new VBox(30);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.setMaxWidth(600);
        
        // Show knight reaction animation
        ImageView knightReaction = isCorrect ? 
            AdventurerAvatar.createAvatar(120, AdventurerAvatar.KnightPose.JUMP) :
            AdventurerAvatar.createAvatar(120, AdventurerAvatar.KnightPose.HURT);
        
        // Result icon
        Label resultIcon = new Label(isCorrect ? "✅" : "❌");
        resultIcon.setStyle("-fx-font-size: 100px;");
        
        // Result message
        Label resultMessage = new Label(isCorrect ? "Correct!" : "Not quite right");
        resultMessage.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (isCorrect ? "#10B981" : "#EF4444") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Coins earned/lost
        Label coinsLabel = new Label((coinChange >= 0 ? "+" : "") + coinChange + " Coins");
        coinsLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (coinChange >= 0 ? "#F59E0B" : "#EF4444") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Explanation
        VBox explanationBox = new VBox(10);
        explanationBox.setAlignment(Pos.CENTER);
        explanationBox.setPadding(new Insets(20));
        explanationBox.setMaxWidth(550);
        explanationBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 12;"
        );
        
        Label explanationText = new Label(explanation);
        explanationText.setWrapText(true);
        explanationText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        explanationBox.getChildren().add(explanationText);
        
        // Next button
        Button nextButton = new Button("Next Question");
        nextButton.setPrefWidth(200);
        nextButton.setPrefHeight(50);
        nextButton.setStyle(
            "-fx-background-color: #667EEA;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        nextButton.setOnAction(e -> {
            currentQuestionIndex++;
            showQuestion();
        });
        
        resultBox.getChildren().addAll(knightReaction, resultIcon, resultMessage, coinsLabel, explanationBox, nextButton);
        contentArea.getChildren().add(resultBox);
        
        // Scale animation for result icon
        ScaleTransition scale = new ScaleTransition(Duration.millis(400), resultIcon);
        scale.setFromX(0);
        scale.setFromY(0);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();
    }
    
    private void completeLevel() {
        contentArea.getChildren().clear();
        
        // Mark level as complete
        gameState.completeLevel(currentLevel.getLevelId());
        gameState.addCoins(currentLevel.getCompletionReward());
        
        // Award completion bonus
        currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + currentLevel.getCompletionReward());
        sessionCoins += currentLevel.getCompletionReward();
        updateCoinDisplay();
        
        // Save progress
        progressService.saveGameState(gameState);
        progressService.awardCoins(currentUser, 0); // Sync user balance
        
        VBox completeBox = new VBox(30);
        completeBox.setAlignment(Pos.CENTER);
        completeBox.setMaxWidth(600);
        
        // Victorious knight!
        ImageView victoriousKnight = AdventurerAvatar.createAvatar(150, AdventurerAvatar.KnightPose.ATTACK_3);
        
        Label completeIcon = new Label("🎉");
        completeIcon.setStyle("-fx-font-size: 120px;");
        
        Label completeTitle = new Label("Level Complete!");
        completeTitle.setStyle(
            "-fx-font-size: 36px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        VBox statsBox = new VBox(12);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(30));
        statsBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        Label bonusLabel = new Label("🎁 Completion Bonus: +" + currentLevel.getCompletionReward() + " Coins");
        bonusLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label totalLabel = new Label("💰 Total Earned: +" + sessionCoins + " Coins");
        totalLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #10B981;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label accuracyLabel = new Label("🎯 Accuracy: " + gameState.getAccuracyPercentage() + "%");
        accuracyLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        statsBox.getChildren().addAll(bonusLabel, totalLabel, accuracyLabel);
        
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = new Button("Back to Levels");
        backButton.setPrefWidth(180);
        backButton.setPrefHeight(50);
        backButton.setStyle(
            "-fx-background-color: #667EEA;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        backButton.setOnAction(e -> {
            contentArea.setStyle(""); // Reset background
            showLevelSelect();
        });
        
        buttonBox.getChildren().add(backButton);
        
        completeBox.getChildren().addAll(victoriousKnight, completeIcon, completeTitle, statsBox, buttonBox);
        contentArea.getChildren().add(completeBox);
        
        // Scale animation
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), completeIcon);
        scale.setFromX(0);
        scale.setFromY(0);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();
    }
    
    /**
     * Create NPC display - uses sprite image if available, otherwise emoji
     */
    private javafx.scene.Node createNPCDisplay() {
        String imagePath = currentLevel.getNpcImagePath();
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.Image npcImage = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(imagePath)
                );
                
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(npcImage);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setStyle(
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 16, 0, 0, 8);"
                );
                
                System.out.println("✅ Loaded NPC sprite: " + imagePath);
                return imageView;
                
            } catch (Exception e) {
                System.err.println("⚠️ Could not load NPC sprite, using emoji fallback: " + e.getMessage());
            }
        }
        
        // Fallback to emoji
        Label emojiLabel = new Label(currentLevel.getNpcEmoji());
        emojiLabel.setStyle("-fx-font-size: 120px;");
        return emojiLabel;
    }
    
    private void updateCoinDisplay() {
        coinLabel.setText("💰 " + currentUser.getSmartCoinBalance() + " Coins");
        
        // Pulse animation
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), coinLabel);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }
    
    public void show() {
        stage.show();
    }
    
    public Stage getStage() {
        return stage;
    }

    private void startLoop() {
        if (!paused && gameLoop != null) {
            gameLoop.start();
        }
    }

    private void stopLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            stopLoop();
            showPauseOverlay();
        } else {
            hidePauseOverlay();
            startLoop();
        }
    }

    private void showPauseOverlay() {
        // Simple overlay indicator at top of content area
        Label overlay = new Label("⏸ Paused - Press ESC to Resume");
        overlay.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 8;"
        );
        overlay.setUserData("__pause_overlay");
        if (!contentArea.getChildren().isEmpty()) {
            contentArea.getChildren().add(0, overlay);
        } else {
            contentArea.getChildren().add(overlay);
        }
    }

    private void hidePauseOverlay() {
        contentArea.getChildren().removeIf(n ->
            n instanceof Label l && "__pause_overlay".equals(l.getUserData())
        );
    }

    // legacy brush kept for reference (unused) — keep signature to avoid future breakage
    @SuppressWarnings("unused")
    private void handleBrush(double x, double y, boolean paint, boolean erase, int tileSize, boolean[][] grid) { }

    // Removed unused brush handler

    // Removed unused nineMax method

    private boolean vegetationAllowedOnTile(java.util.Random rng) {
        return rng.nextDouble() < 0.04; // 4% chance
    }

    private void addObstacle(Image img, javafx.scene.layout.Pane layer, int col, int row, int wTiles, int hTiles, int tileSize, TileCollisionMap collision) {
        ImageView iv = new ImageView(img);
        iv.setFitWidth(wTiles * tileSize);
        iv.setFitHeight(hTiles * tileSize);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setLayoutX(col * tileSize);
        iv.setLayoutY(row * tileSize);
        layer.getChildren().add(iv);
        for (int r = 0; r < hTiles; r++) {
            for (int c = 0; c < wTiles; c++) {
                collision.setBlocked(col + c, row + r, true);
            }
        }
    }
}

