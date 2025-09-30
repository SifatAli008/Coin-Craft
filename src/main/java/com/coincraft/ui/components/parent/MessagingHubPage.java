package com.coincraft.ui.components.parent;

import java.util.List;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Messaging hub for merchants/parents.
 * - One-to-one messaging: opens AdventureMessagingPortal per child
 * - One-to-many broadcast: simple textarea to send a message to all children (MVP: console/log)
 */
public class MessagingHubPage {
    private final User currentParent;
    private final List<User> children;
    private VBox root;

    public MessagingHubPage(User currentParent, List<User> children) {
        this.currentParent = currentParent;
        this.children = children;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(12));

        Label title = new Label("Messaging");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 800;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );

        // One-to-one list
        VBox list = new VBox(10);
        list.setPadding(new Insets(10));
        list.setStyle(
            "-fx-background-color: rgba(255,255,255,0.96);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #E5E7EB;"
        );

        Label listTitle = new Label("Direct Messages");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #0F172A;");
        list.getChildren().add(listTitle);

        if (children != null && !children.isEmpty()) {
            for (User child : children) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(8, 4, 8, 4));

                Label name = new Label(child.getName() + (child.getUsername() != null ? "  (" + child.getUsername() + ")" : ""));
                name.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151; -fx-font-weight: 700;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button msgBtn = new Button("Message");
                msgBtn.setStyle(
                    "-fx-background-color: #2196F3;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 6 12;"
                );
                msgBtn.setOnAction(e -> openDirectMessage(child));

                row.getChildren().addAll(name, spacer, msgBtn);
                list.getChildren().add(row);
            }
        } else {
            Label empty = new Label("No adventurers found.");
            empty.setStyle("-fx-text-fill: #6B7280;");
            list.getChildren().add(empty);
        }

        ScrollPane listScroll = new ScrollPane(list);
        listScroll.setFitToWidth(true);
        listScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Broadcast card
        VBox broadcast = new VBox(10);
        broadcast.setPadding(new Insets(12));
        broadcast.setStyle(
            "-fx-background-color: rgba(255,255,255,0.96);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #FFE0B2;" +
            "-fx-border-width: 2;"
        );
        Label bTitle = new Label("Group Broadcast to All Adventurers");
        bTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #D97706;");

        TextArea bInput = new TextArea();
        bInput.setPromptText("Write an announcement or message for all your adventurers...");
        bInput.setPrefRowCount(3);

        Button sendAll = new Button("Send to All");
        sendAll.setStyle(
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 800;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 16;"
        );
        sendAll.setOnAction(e -> {
            String text = bInput.getText() != null ? bInput.getText().trim() : "";
            if (text.isEmpty()) return;
            // MVP: Just log. Future: persist to Firebase and notify via websockets.
            System.out.println("[Broadcast by " + (currentParent != null ? currentParent.getName() : "Parent") + "]: " + text);
            bInput.clear();
        });

        broadcast.getChildren().addAll(bTitle, bInput, sendAll);

        root.getChildren().addAll(title, listScroll, broadcast);
    }

    private void openDirectMessage(User child) {
        Stage stage = (Stage) root.getScene().getWindow();
        AdventureMessagingPortal portal = new AdventureMessagingPortal(stage, child);
        portal.show();
    }

    public VBox getRoot() {
        return root;
    }
}


