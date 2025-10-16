package com.coincraft.game.adventure.zones;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.coincraft.game.adventure.models.AdventureZone;
import com.coincraft.game.adventure.models.AdventurePlayer;
import com.coincraft.game.adventure.models.AdventureNPC;
import com.coincraft.game.adventure.models.AdventureInteractable;

/**
 * Savings Summit - Mountain zone teaching savings concepts
 * Similar to Echo Quest's themed zones but with financial education
 */
public class SavingsSummitZone extends AdventureZone {
    
    public SavingsSummitZone() {
        super("Savings Summit", "Climb the mountain of financial wisdom and learn about saving");
        this.zoneColor = Color.LIGHTGREEN;
        this.backgroundStyle = "linear-gradient(135deg, #90EE90 0%, #228B22 100%)";
    }
    
    @Override
    protected void setupZone() {
        // Add Greta the Goalkeeper NPC
        AdventureNPC greta = new AdventureNPC(
            "Greta the Goalkeeper", 
            "🥅", 
            "Welcome to Savings Summit! I'll teach you about setting and reaching your savings goals.",
            400, 300
        );
        npcs.add(greta);
        
        // Add goal-setting station
        AdventureInteractable goalStation = new AdventureInteractable(
            "Goal Setting Station",
            "🎯",
            "Set your financial goals here",
            600, 200
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showGoalSettingLesson(player);
            }
        };
        interactables.add(goalStation);
        
        // Add savings calculator
        AdventureInteractable savingsCalculator = new AdventureInteractable(
            "Savings Calculator",
            "🧮",
            "Calculate your savings potential",
            300, 500
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showSavingsCalculatorLesson(player);
            }
        };
        interactables.add(savingsCalculator);
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add mountain peaks
        for (int i = 0; i < 3; i++) {
            Rectangle peak = new Rectangle(
                200 + i * 300,
                400 - i * 100,
                100,
                200 + i * 50
            );
            peak.setFill(Color.GRAY);
            peak.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(peak);
        }
        
        // Add trees
        for (int i = 0; i < 8; i++) {
            Rectangle tree = new Rectangle(
                50 + i * 150,
                500 + Math.random() * 100,
                15,
                80
            );
            tree.setFill(Color.BROWN);
            tree.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(tree);
            
            Circle leaves = new Circle(
                57 + i * 150,
                480 + Math.random() * 100,
                25,
                Color.GREEN
            );
            leaves.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(leaves);
        }
        
        // Add zone title
        Text title = new Text(50, 50, "🏔️ " + zoneName);
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Check if player has learned about savings goals
        if (player.getCenterX() > 800 && player.getCenterY() < 300) {
            setCompleted(true);
        }
    }
    
    private void showGoalSettingLesson(AdventurePlayer player) {
        System.out.println("🎯 Goal Setting Lesson: SMART Goals");
        System.out.println("S - Specific: 'Save $50 for a new toy'");
        System.out.println("M - Measurable: Track your progress");
        System.out.println("A - Achievable: Set realistic goals");
        System.out.println("R - Relevant: Goals that matter to you");
        System.out.println("T - Time-bound: Set a deadline!");
    }
    
    private void showSavingsCalculatorLesson(AdventurePlayer player) {
        System.out.println("🧮 Savings Calculator: The Power of Compound Interest");
        System.out.println("If you save $10 every week:");
        System.out.println("• After 1 year: $520");
        System.out.println("• After 5 years: $2,600");
        System.out.println("• With 5% interest: $2,900!");
    }
}
