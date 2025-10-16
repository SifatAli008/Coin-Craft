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
 * Banking Bayou - Swamp zone teaching banking fundamentals
 * Similar to Echo Quest's themed zones but with banking education
 */
public class BankingBayouZone extends AdventureZone {
    
    public BankingBayouZone() {
        super("Banking Bayou", "Navigate the murky waters of banking and learn financial fundamentals");
        this.zoneColor = Color.DARKGREEN;
        this.backgroundStyle = "linear-gradient(135deg, #8FBC8F 0%, #2F4F2F 100%)";
    }
    
    @Override
    protected void setupZone() {
        // Add Bella the Banker NPC
        AdventureNPC bella = new AdventureNPC(
            "Bella the Banker", 
            "🏦", 
            "Welcome to Banking Bayou! I'll teach you about banks, accounts, and how money flows through the system.",
            400, 350
        );
        npcs.add(bella);
        
        // Add ATM machine
        AdventureInteractable atm = new AdventureInteractable(
            "ATM Machine",
            "🏧",
            "Learn about automated banking",
            200, 500
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showATMLesson();
            }
        };
        interactables.add(atm);
        
        // Add bank vault
        AdventureInteractable vault = new AdventureInteractable(
            "Bank Vault",
            "🔒",
            "Discover how banks keep your money safe",
            600, 300
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showBankingSafetyLesson();
            }
        };
        interactables.add(vault);
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add swamp trees
        for (int i = 0; i < 10; i++) {
            Rectangle tree = new Rectangle(
                50 + i * 120,
                500 + Math.random() * 150,
                25,
                100 + Math.random() * 50
            );
            tree.setFill(Color.DARKGREEN);
            tree.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(tree);
            
            Circle leaves = new Circle(
                62 + i * 120,
                480 + Math.random() * 150,
                30 + Math.random() * 20,
                Color.FORESTGREEN
            );
            leaves.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(leaves);
        }
        
        // Add swamp water
        for (int i = 0; i < 20; i++) {
            Circle waterRipple = new Circle(
                Math.random() * 1200,
                600 + Math.random() * 200,
                10 + Math.random() * 20,
                Color.DARKGREEN.deriveColor(0, 1, 1, 0.3)
            );
            waterRipple.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(waterRipple);
        }
        
        // Add zone title
        Text title = new Text(50, 50, "🌊 " + zoneName);
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Check if player has learned about banking
        if (player.getCenterX() > 500 && player.getCenterY() > 400) {
            setCompleted(true);
        }
    }
    
    private void showATMLesson() {
        System.out.println("🏧 ATM Lesson: Automated Banking");
        System.out.println("ATMs let you:");
        System.out.println("• Withdraw cash from your account");
        System.out.println("• Check your balance");
        System.out.println("• Deposit money");
        System.out.println("• Transfer money between accounts");
        System.out.println("Always keep your PIN secret!");
    }
    
    private void showBankingSafetyLesson() {
        System.out.println("🔒 Banking Safety: Protecting Your Money");
        System.out.println("Banks keep your money safe by:");
        System.out.println("• FDIC insurance up to $250,000");
        System.out.println("• Strong security systems");
        System.out.println("• Fraud protection");
        System.out.println("• Multiple backup systems");
        System.out.println("Your money is much safer in a bank than under your mattress!");
    }
}
