package com.coincraft.game.adventure.zones;

import javafx.scene.paint.Color;
import com.coincraft.game.adventure.models.EchoQuestZone;
import com.coincraft.game.adventure.models.EchoQuestNPC;
import com.coincraft.game.adventure.models.EchoQuestInteractable;

/**
 * Whispering Woods Zone
 * The first therapeutic zone in Echo Quest style adventure
 * Theme: Mindfulness and emotional awareness
 * 
 * Features:
 * - Calm forest environment
 * - Breathing exercise NPCs
 * - Mind Shard collection points
 * - Therapeutic dialogue and interactions
 */
public class WhisperingWoodsZone extends EchoQuestZone {
    
    public WhisperingWoodsZone() {
        super("Whispering Woods", "A peaceful forest where you can learn mindfulness and emotional awareness");
        this.zoneColor = Color.LIGHTGREEN;
        this.backgroundStyle = "linear-gradient(135deg, #90EE90 0%, #98FB98 50%, #87CEEB 100%)";
    }
    
    @Override
    protected void setupTherapeuticElements() {
        // Add Mindfulness Guide NPC
        EchoQuestNPC mindfulnessGuide = new EchoQuestNPC(
            "Mindfulness Guide", 
            "🧘‍♀️", 
            "Welcome to the Whispering Woods. Here, you'll learn the art of mindfulness and emotional awareness. Take deep breaths and let the forest guide you to inner peace.",
            200, 300
        );
        addNPC(mindfulnessGuide);
        
        // Add Breathing Exercise Station
        EchoQuestInteractable breathingStation = new EchoQuestInteractable(
            "Breathing Station",
            "🌬️",
            "A peaceful spot for breathing exercises. Press SPACE to begin a guided breathing session.",
            400, 200
        ) {
            @Override
            public void onInteract() {
                showBreathingExercise();
            }
        };
        addInteractable(breathingStation);
        
        // Add Mind Shard Collection Point
        EchoQuestInteractable mindShardPoint = new EchoQuestInteractable(
            "Mind Shard",
            "✨",
            "A glowing Mind Shard representing emotional clarity. Collect it to restore balance.",
            600, 400
        ) {
            @Override
            public void onInteract() {
                collectMindShard();
            }
        };
        addInteractable(mindShardPoint);
        
        // Add Emotional Awareness Tree
        EchoQuestInteractable awarenessTree = new EchoQuestInteractable(
            "Awareness Tree",
            "🌳",
            "An ancient tree that helps you understand your emotions. Interact to learn about emotional awareness.",
            300, 500
        ) {
            @Override
            public void onInteract() {
                showEmotionalAwarenessLesson();
            }
        };
        addInteractable(awarenessTree);
        
        // Add Meditation Circle
        EchoQuestInteractable meditationCircle = new EchoQuestInteractable(
            "Meditation Circle",
            "🕯️",
            "A sacred circle for meditation and reflection. Enter to begin a mindfulness session.",
            500, 600
        ) {
            @Override
            public void onInteract() {
                startMeditationSession();
            }
        };
        addInteractable(meditationCircle);
    }
    
    private void showBreathingExercise() {
        System.out.println("🌬️ Breathing Exercise Started");
        System.out.println("Inhale slowly for 4 counts...");
        System.out.println("Hold for 4 counts...");
        System.out.println("Exhale slowly for 4 counts...");
        System.out.println("Repeat this cycle to find inner peace.");
    }
    
    private void collectMindShard() {
        System.out.println("✨ Mind Shard Collected!");
        System.out.println("You feel a wave of emotional clarity wash over you.");
        System.out.println("Your inner balance is restored.");
    }
    
    private void showEmotionalAwarenessLesson() {
        System.out.println("🌳 Emotional Awareness Lesson");
        System.out.println("Emotions are like weather - they come and go.");
        System.out.println("By observing them without judgment, we can find peace.");
        System.out.println("Remember: You are not your emotions; you are the observer.");
    }
    
    private void startMeditationSession() {
        System.out.println("🕯️ Meditation Session Begins");
        System.out.println("Find a comfortable position and close your eyes.");
        System.out.println("Focus on your breath and let thoughts pass like clouds.");
        System.out.println("When you're ready, open your eyes and continue your journey.");
    }
}
