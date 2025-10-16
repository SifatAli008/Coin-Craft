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
 * Investment Island - Tropical zone teaching investment basics
 * Similar to Echo Quest's exploration zones but with investment education
 */
public class InvestmentIslandZone extends AdventureZone {
    
    public InvestmentIslandZone() {
        super("Investment Island", "Discover the secrets of growing your money on this tropical paradise");
        this.zoneColor = Color.LIGHTYELLOW;
        this.backgroundStyle = "linear-gradient(135deg, #FFE4B5 0%, #DEB887 100%)";
    }
    
    @Override
    protected void setupZone() {
        // Add Captain Coinbeard NPC
        AdventureNPC captainCoinbeard = new AdventureNPC(
            "Captain Coinbeard", 
            "🏴‍☠️", 
            "Ahoy! Welcome to Investment Island! I'll teach you how to make your money work for you.",
            500, 400
        );
        npcs.add(captainCoinbeard);
        
        // Add investment simulator
        AdventureInteractable investmentSimulator = new AdventureInteractable(
            "Investment Simulator",
            "📈",
            "Practice investing with virtual money",
            300, 300
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showInvestmentLesson();
            }
        };
        interactables.add(investmentSimulator);
        
        // Add treasure chest
        AdventureInteractable treasureChest = new AdventureInteractable(
            "Treasure Chest",
            "💰",
            "Learn about different types of investments",
            700, 200
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showInvestmentTypesLesson();
            }
        };
        interactables.add(treasureChest);
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add palm trees
        for (int i = 0; i < 6; i++) {
            Rectangle tree = new Rectangle(
                100 + i * 200,
                600,
                20,
                120
            );
            tree.setFill(Color.BROWN);
            tree.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(tree);
            
            Circle leaves = new Circle(
                110 + i * 200,
                570,
                40,
                Color.GREEN
            );
            leaves.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(leaves);
        }
        
        // Add beach
        Rectangle beach = new Rectangle(0, 700, 1200, 100);
        beach.setFill(Color.SANDYBROWN);
        beach.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(beach);
        
        // Add water effects
        for (int i = 0; i < 15; i++) {
            Circle waterRipple = new Circle(
                Math.random() * 1200,
                650 + Math.random() * 50,
                8 + Math.random() * 12,
                Color.CYAN.deriveColor(0, 1, 1, 0.4)
            );
            waterRipple.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(waterRipple);
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
        // Check if player has learned about investments
        if (player.getCenterX() > 600 && player.getCenterY() < 400) {
            setCompleted(true);
        }
    }
    
    private void showInvestmentLesson() {
        System.out.println("📈 Investment Lesson: Making Money Work for You");
        System.out.println("Investments can grow your money over time:");
        System.out.println("• Stocks: Own part of a company");
        System.out.println("• Bonds: Lend money to companies/governments");
        System.out.println("• Savings accounts: Safe but slow growth");
        System.out.println("Remember: Higher risk = Higher potential reward!");
    }
    
    private void showInvestmentTypesLesson() {
        System.out.println("💰 Investment Types: Diversification is Key");
        System.out.println("Don't put all your eggs in one basket:");
        System.out.println("• Spread your investments");
        System.out.println("• Start with low-risk options");
        System.out.println("• Learn before you invest");
        System.out.println("• Be patient - investments take time to grow!");
    }
}
