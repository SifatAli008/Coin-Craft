package com.coincraft.ui.util;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.ui.theme.PixelButton;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

/**
 * Utility to attach selection sounds across controls.
 */
public final class UiSoundBindings {
    private UiSoundBindings() {}

    public static void install(Parent root) {
        if (root == null) return;
        installRecursive(root);
        // Also react to dynamic children additions
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                installRecursive(root);
            }
        });
    }

    private static void installRecursive(Parent parent) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            attach(node);
            if (node instanceof Parent) {
                installRecursive((Parent) node);
            }
        }
    }

    private static void attach(Node node) {
        // Global binding for all JavaFX buttons (except PixelButton which already plays sound)
        if (node instanceof ButtonBase && !(node instanceof PixelButton)) {
            ButtonBase button = (ButtonBase) node;
            button.addEventHandler(ActionEvent.ACTION, e -> CentralizedMusicManager.getInstance().playButtonClick());
        }

        if (node instanceof TextInputControl || node instanceof ComboBoxBase || node instanceof ChoiceBox || node instanceof ListView || node instanceof TableView || node instanceof TreeView || node instanceof Slider || node instanceof Spinner) {
            node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> CentralizedMusicManager.getInstance().playInputSelect());
        }
    }
}


