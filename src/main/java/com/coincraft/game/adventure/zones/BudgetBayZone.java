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
 * Budget Bay - First zone teaching basic budgeting concepts
 * Similar to Echo Quest's Whispering Woods but with financial themes
 */
public class BudgetBayZone extends AdventureZone {
    
    public BudgetBayZone() {
        super("Budget Bay", "Learn the basics of budgeting in this peaceful bay");
        this.zoneColor = Color.LIGHTBLUE;
        this.backgroundStyle = "linear-gradient(135deg, #87CEEB 0%, #4682B4 100%)";
    }
    
    @Override
    protected void setupZone() {
        // Add Elder Pennywise NPC
        AdventureNPC elderPennywise = new AdventureNPC(
            "Elder Pennywise", 
            "🧙‍♂️", 
            "Welcome to Budget Bay! Let me teach you about smart money management.",
            300, 200
        );
        npcs.add(elderPennywise);
        
        // Add interactive budget calculator
        AdventureInteractable budgetCalculator = new AdventureInteractable(
            "Budget Calculator",
            "💰",
            "Click to learn about budgeting",
            500, 400
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showBudgetLesson();
            }
        };
        interactables.add(budgetCalculator);
        
        // Add savings jar
        AdventureInteractable savingsJar = new AdventureInteractable(
            "Savings Jar",
            "🏺",
            "A place to save your SmartCoins",
            700, 300
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showSavingsLesson();
            }
        };
        interactables.add(savingsJar);
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add water effects
        for (int i = 0; i < 20; i++) {
            Circle waterRipple = new Circle(
                Math.random() * 1200,
                Math.random() * 800,
                5 + Math.random() * 15,
                Color.CYAN.deriveColor(0, 1, 1, 0.3)
            );
            waterRipple.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(waterRipple);
        }
        
        // Add palm trees
        for (int i = 0; i < 5; i++) {
            Rectangle tree = new Rectangle(
                100 + i * 200,
                600,
                20,
                100
            );
            tree.setFill(Color.BROWN);
            tree.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(tree);
            
            Circle leaves = new Circle(
                110 + i * 200,
                580,
                30,
                Color.GREEN
            );
            leaves.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(leaves);
        }
        
        // Add zone title
        Text title = new Text(50, 50, "🏝️ " + zoneName);
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Check if player has learned about budgeting
        // This would be tracked in the player's progress
        // For now, we'll use a simple completion check
        if (player.getCenterX() > 800 && player.getCenterY() > 600) {
            setCompleted(true);
        }
    }
    
    private void showBudgetLesson() {
        // Show budget lesson dialog
        System.out.println("📊 Budget Lesson: Needs vs Wants");
        System.out.println("Needs: Food, shelter, clothing");
        System.out.println("Wants: Toys, games, treats");
        System.out.println("Smart budgeting means prioritizing needs first!");
        
        // Award coins for learning
        // This would integrate with the main game's coin system
    }
    
    private void showSavingsLesson() {
        // Show savings lesson dialog
        System.out.println("💰 Savings Lesson: The Power of Saving");
        System.out.println("Saving money helps you:");
        System.out.println("• Buy bigger things later");
        System.out.println("• Handle emergencies");
        System.out.println("• Reach your goals!");
        
        // Award coins for learning
    }
}
