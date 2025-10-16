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
 * Digital Safety Zone - Tech zone teaching online financial safety
 * Similar to Echo Quest's themed zones but with digital safety education
 */
public class DigitalSafetyZone extends AdventureZone {
    
    public DigitalSafetyZone() {
        super("Digital Safety", "Navigate the digital realm and learn to protect yourself online");
        this.zoneColor = Color.PURPLE;
        this.backgroundStyle = "linear-gradient(135deg, #DDA0DD 0%, #8B008B 100%)";
    }
    
    @Override
    protected void setupZone() {
        // Add Scarlet Shield NPC
        AdventureNPC scarletShield = new AdventureNPC(
            "Scarlet Shield", 
            "🛡️", 
            "Welcome to the Digital Safety Zone! I'll teach you how to protect yourself and your money online.",
            350, 400
        );
        npcs.add(scarletShield);
        
        // Add password generator
        AdventureInteractable passwordGenerator = new AdventureInteractable(
            "Password Generator",
            "🔐",
            "Learn about strong passwords",
            200, 300
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showPasswordLesson();
            }
        };
        interactables.add(passwordGenerator);
        
        // Add phishing detector
        AdventureInteractable phishingDetector = new AdventureInteractable(
            "Phishing Detector",
            "🎣",
            "Learn to spot fake emails and websites",
            500, 200
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showPhishingLesson();
            }
        };
        interactables.add(phishingDetector);
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add tech elements
        for (int i = 0; i < 8; i++) {
            Rectangle techBlock = new Rectangle(
                100 + i * 150,
                100 + Math.random() * 200,
                80,
                60
            );
            techBlock.setFill(Color.DARKBLUE);
            techBlock.setStroke(Color.CYAN);
            techBlock.setStrokeWidth(2);
            techBlock.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(techBlock);
        }
        
        // Add digital particles
        for (int i = 0; i < 30; i++) {
            Circle digitalParticle = new Circle(
                Math.random() * 1200,
                Math.random() * 800,
                3 + Math.random() * 5,
                Color.CYAN.deriveColor(0, 1, 1, 0.6)
            );
            digitalParticle.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(digitalParticle);
        }
        
        // Add zone title
        Text title = new Text(50, 50, "💻 " + zoneName);
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Check if player has learned about digital safety
        if (player.getCenterX() > 400 && player.getCenterY() < 500) {
            setCompleted(true);
        }
    }
    
    private void showPasswordLesson() {
        System.out.println("🔐 Password Security: Protecting Your Accounts");
        System.out.println("Strong passwords should:");
        System.out.println("• Be at least 8 characters long");
        System.out.println("• Include uppercase and lowercase letters");
        System.out.println("• Include numbers and symbols");
        System.out.println("• Be unique for each account");
        System.out.println("• Never be shared with anyone!");
    }
    
    private void showPhishingLesson() {
        System.out.println("🎣 Phishing Protection: Spot the Scams");
        System.out.println("Watch out for:");
        System.out.println("• Emails asking for personal information");
        System.out.println("• Urgent messages about your account");
        System.out.println("• Links that don't look right");
        System.out.println("• Requests for passwords or PINs");
        System.out.println("When in doubt, contact the company directly!");
    }
}
