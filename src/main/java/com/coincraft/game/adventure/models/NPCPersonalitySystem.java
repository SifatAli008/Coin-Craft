package com.coincraft.game.adventure.models;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Advanced personality system for NPCs with dynamic mood, preferences, and adaptive behavior
 */
public class NPCPersonalitySystem {
    
    public enum PersonalityType {
        ADVENTUROUS("Adventurous", "🗡️", "Bold, risk-taking, loves challenges"),
        WISE("Wise", "🧙‍♀️", "Knowledgeable, patient, loves teaching"),
        BUSINESS("Business", "💼", "Analytical, goal-oriented, loves efficiency"),
        FRIENDLY("Friendly", "😊", "Warm, encouraging, loves helping"),
        MYSTERIOUS("Mysterious", "🤔", "Enigmatic, thoughtful, loves puzzles");
        
        private final String displayName;
        private final String emoji;
        private final String description;
        
        PersonalityType(String displayName, String emoji, String description) {
            this.displayName = displayName;
            this.emoji = emoji;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getEmoji() { return emoji; }
        public String getDescription() { return description; }
    }
    
    public enum Mood {
        HAPPY("Happy", "😊", 0.8, "Very positive and encouraging"),
        EXCITED("Excited", "🤩", 0.9, "Enthusiastic and energetic"),
        CONTENT("Content", "😌", 0.6, "Satisfied and calm"),
        NEUTRAL("Neutral", "😐", 0.5, "Balanced and professional"),
        CONCERNED("Concerned", "😟", 0.3, "Worried but helpful"),
        FRUSTRATED("Frustrated", "😤", 0.2, "Impatient and direct");
        
        private final String displayName;
        private final String emoji;
        private final double energyLevel;
        private final String description;
        
        Mood(String displayName, String emoji, double energyLevel, String description) {
            this.displayName = displayName;
            this.emoji = emoji;
            this.energyLevel = energyLevel;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getEmoji() { return emoji; }
        public double getEnergyLevel() { return energyLevel; }
        public String getDescription() { return description; }
    }
    
    public static class PersonalityProfile {
        private final PersonalityType basePersonality;
        private Mood currentMood;
        private final Map<String, Double> topicPreferences;
        private final Map<String, Integer> interactionHistory;
        private final List<String> favoriteTopics;
        private final List<String> dislikedTopics;
        private double patienceLevel;
        private double enthusiasmLevel;
        private int totalInteractions;
        private int successfulInteractions;
        private final Random random;
        
        public PersonalityProfile(PersonalityType personality) {
            this.basePersonality = personality;
            this.currentMood = Mood.NEUTRAL;
            this.topicPreferences = new HashMap<>();
            this.interactionHistory = new HashMap<>();
            this.favoriteTopics = new ArrayList<>();
            this.dislikedTopics = new ArrayList<>();
            this.patienceLevel = 0.7;
            this.enthusiasmLevel = 0.6;
            this.totalInteractions = 0;
            this.successfulInteractions = 0;
            this.random = new Random();
            
            initializePersonality();
        }
        
        private void initializePersonality() {
            switch (basePersonality) {
                case ADVENTUROUS -> {
                    topicPreferences.put("challenges", 0.9);
                    topicPreferences.put("risk", 0.8);
                    topicPreferences.put("strategy", 0.7);
                    favoriteTopics.add("adventure");
                    favoriteTopics.add("challenges");
                    favoriteTopics.add("risk management");
                    patienceLevel = 0.6;
                    enthusiasmLevel = 0.9;
                }
                case WISE -> {
                    topicPreferences.put("education", 0.9);
                    topicPreferences.put("wisdom", 0.8);
                    topicPreferences.put("learning", 0.8);
                    favoriteTopics.add("financial literacy");
                    favoriteTopics.add("education");
                    favoriteTopics.add("wisdom");
                    patienceLevel = 0.9;
                    enthusiasmLevel = 0.7;
                }
                case BUSINESS -> {
                    topicPreferences.put("efficiency", 0.9);
                    topicPreferences.put("analysis", 0.8);
                    topicPreferences.put("results", 0.8);
                    favoriteTopics.add("business");
                    favoriteTopics.add("analysis");
                    favoriteTopics.add("efficiency");
                    patienceLevel = 0.5;
                    enthusiasmLevel = 0.6;
                }
                case FRIENDLY -> {
                    topicPreferences.put("help", 0.9);
                    topicPreferences.put("encouragement", 0.8);
                    topicPreferences.put("support", 0.8);
                    favoriteTopics.add("help");
                    favoriteTopics.add("encouragement");
                    favoriteTopics.add("support");
                    patienceLevel = 0.8;
                    enthusiasmLevel = 0.8;
                }
                case MYSTERIOUS -> {
                    topicPreferences.put("puzzles", 0.9);
                    topicPreferences.put("mystery", 0.8);
                    topicPreferences.put("thinking", 0.7);
                    favoriteTopics.add("puzzles");
                    favoriteTopics.add("mystery");
                    favoriteTopics.add("deep thinking");
                    patienceLevel = 0.7;
                    enthusiasmLevel = 0.5;
                }
            }
        }
        
        /**
         * Update mood based on interaction outcome
         */
        public void updateMood(boolean successful, String topic) {
            totalInteractions++;
            if (successful) {
                successfulInteractions++;
            }
            
            // Track interaction history for this topic
            interactionHistory.put(topic, interactionHistory.getOrDefault(topic, 0) + 1);
            
            // Update topic preferences based on interaction
            double currentPref = topicPreferences.getOrDefault(topic, 0.5);
            if (successful) {
                topicPreferences.put(topic, Math.min(1.0, currentPref + 0.1));
            } else {
                topicPreferences.put(topic, Math.max(0.0, currentPref - 0.05));
            }
            
            // Update mood based on recent performance
            double recentSuccessRate = getRecentSuccessRate();
            if (recentSuccessRate > 0.8) {
                currentMood = Mood.EXCITED;
            } else if (recentSuccessRate > 0.6) {
                currentMood = Mood.HAPPY;
            } else if (recentSuccessRate > 0.4) {
                currentMood = Mood.CONTENT;
            } else if (recentSuccessRate > 0.2) {
                currentMood = Mood.CONCERNED;
            } else {
                currentMood = Mood.FRUSTRATED;
            }
            
            // Adjust patience and enthusiasm
            patienceLevel = Math.max(0.1, patienceLevel + (successful ? 0.05 : -0.1));
            enthusiasmLevel = Math.max(0.1, enthusiasmLevel + (successful ? 0.1 : -0.05));
        }
        
        /**
         * Get personalized greeting based on personality and mood
         */
        public String getPersonalizedGreeting() {
            String baseGreeting = getBaseGreeting();
            String moodModifier = getMoodModifier();
            
            // Add some variety with random elements
            if (random.nextDouble() < 0.3) { // 30% chance for variety
                String variety = getRandomVariety();
                return baseGreeting + " " + variety + " " + moodModifier;
            }
            
            return baseGreeting + " " + moodModifier;
        }
        
        /**
         * Get random variety element for greetings
         */
        private String getRandomVariety() {
            String[] varieties = {
                "What brings you here today?",
                "I've been expecting you.",
                "Perfect timing!",
                "How wonderful to see you!",
                "I was just thinking about you."
            };
            return varieties[random.nextInt(varieties.length)];
        }
        
        /**
         * Get interaction count for a specific topic
         */
        public int getTopicInteractionCount(String topic) {
            return interactionHistory.getOrDefault(topic, 0);
        }
        
        private String getBaseGreeting() {
            return switch (basePersonality) {
                case ADVENTUROUS -> "Welcome, brave soul!";
                case WISE -> "Greetings, seeker of knowledge.";
                case BUSINESS -> "Good to see you, potential partner.";
                case FRIENDLY -> "Hello there, friend!";
                case MYSTERIOUS -> "Ah, another curious mind approaches.";
                default -> "Hello there!";
            };
        }
        
        private String getMoodModifier() {
            return switch (currentMood) {
                case HAPPY -> "I'm in great spirits today!";
                case EXCITED -> "I'm absolutely thrilled to see you!";
                case CONTENT -> "I'm feeling quite well.";
                case NEUTRAL -> "How may I assist you?";
                case CONCERNED -> "I'm a bit worried about your progress.";
                case FRUSTRATED -> "I hope we can make better progress today.";
                default -> "";
            };
        }
        
        /**
         * Get topic-specific response based on personality
         */
        public String getTopicResponse(String topic, boolean isCorrect) {
            double preference = topicPreferences.getOrDefault(topic, 0.5);
            String baseResponse = getBaseTopicResponse(topic, isCorrect);
            String personalityModifier = getPersonalityModifier(topic, preference);
            String moodModifier = getMoodModifierForTopic(isCorrect);
            
            return baseResponse + " " + personalityModifier + " " + moodModifier;
        }
        
        private String getBaseTopicResponse(String topic, boolean isCorrect) {
            if (isCorrect) {
                return "Excellent work on " + topic + "!";
            } else {
                return "Let me help you understand " + topic + " better.";
            }
        }
        
        private String getPersonalityModifier(String topic, double preference) {
            if (preference > 0.8) {
                return switch (basePersonality) {
                    case ADVENTUROUS -> "I love talking about " + topic + " - it's so exciting!";
                    case WISE -> "Ah, " + topic + " is one of my favorite subjects to teach.";
                    case BUSINESS -> "Perfect, " + topic + " is crucial for success.";
                    case FRIENDLY -> "I'm so happy you're interested in " + topic + "!";
                    case MYSTERIOUS -> "Fascinating that you chose " + topic + "...";
                };
            } else if (preference < 0.3) {
                return switch (basePersonality) {
                    case ADVENTUROUS -> "Well, " + topic + " isn't my favorite, but let's tackle it!";
                    case WISE -> "While " + topic + " isn't my specialty, I'll do my best.";
                    case BUSINESS -> "I'll be direct about " + topic + " - it's not my strength.";
                    case FRIENDLY -> "I'll try my best with " + topic + ", though it's challenging.";
                    case MYSTERIOUS -> "Interesting choice... " + topic + " is quite complex.";
                };
            }
            return "";
        }
        
        private String getMoodModifierForTopic(boolean isCorrect) {
            if (isCorrect) {
                return switch (currentMood) {
                    case EXCITED -> "This is fantastic!";
                    case HAPPY -> "Wonderful!";
                    case CONTENT -> "Good job!";
                    case NEUTRAL -> "Well done.";
                    case CONCERNED -> "That's better.";
                    case FRUSTRATED -> "Finally!";
                };
            } else {
                return switch (currentMood) {
                    case EXCITED -> "Don't worry, we'll get this!";
                    case HAPPY -> "Let's try again!";
                    case CONTENT -> "That's okay, let's learn.";
                    case NEUTRAL -> "Let me explain.";
                    case CONCERNED -> "I'm worried about this.";
                    case FRUSTRATED -> "This is concerning.";
                };
            }
        }
        
        /**
         * Get adaptive difficulty based on personality and performance
         */
        public int getAdaptiveDifficulty() {
            double successRate = getRecentSuccessRate();
            
            // Adjust based on personality
            int baseDifficulty = switch (basePersonality) {
                case ADVENTUROUS -> 2; // Prefers challenges
                case WISE -> 1; // Patient teacher
                case BUSINESS -> 2; // Results-oriented
                case FRIENDLY -> 1; // Encouraging
                case MYSTERIOUS -> 3; // Loves complexity
            };
            
            // Adjust based on performance
            if (successRate > 0.8) {
                return Math.min(5, baseDifficulty + 1);
            } else if (successRate < 0.3) {
                return Math.max(1, baseDifficulty - 1);
            }
            
            return baseDifficulty;
        }
        
        /**
         * Get recent success rate (last 10 interactions)
         */
        private double getRecentSuccessRate() {
            if (totalInteractions == 0) return 0.5;
            return (double) successfulInteractions / totalInteractions;
        }
        
        /**
         * Get personality-based conversation tips
         */
        public List<String> getConversationTips() {
            List<String> tips = new ArrayList<>();
            
            switch (basePersonality) {
                case ADVENTUROUS -> {
                    tips.add("Loves challenges and risk-taking");
                    tips.add("Responds well to bold choices");
                    tips.add("Enjoys discussing strategy");
                }
                case WISE -> {
                    tips.add("Patient and methodical");
                    tips.add("Loves teaching and explaining");
                    tips.add("Appreciates thoughtful questions");
                }
                case BUSINESS -> {
                    tips.add("Values efficiency and results");
                    tips.add("Prefers direct communication");
                    tips.add("Focuses on practical applications");
                }
                case FRIENDLY -> {
                    tips.add("Encouraging and supportive");
                    tips.add("Loves helping others");
                    tips.add("Responds to positive energy");
                }
                case MYSTERIOUS -> {
                    tips.add("Enjoys puzzles and complexity");
                    tips.add("Appreciates deep thinking");
                    tips.add("Likes enigmatic responses");
                }
            }
            
            return tips;
        }
        
        // Getters
        public PersonalityType getBasePersonality() { return basePersonality; }
        public Mood getCurrentMood() { return currentMood; }
        public Map<String, Double> getTopicPreferences() { return topicPreferences; }
        public List<String> getFavoriteTopics() { return favoriteTopics; }
        public List<String> getDislikedTopics() { return dislikedTopics; }
        public double getPatienceLevel() { return patienceLevel; }
        public double getEnthusiasmLevel() { return enthusiasmLevel; }
        public int getTotalInteractions() { return totalInteractions; }
        public int getSuccessfulInteractions() { return successfulInteractions; }
        public double getSuccessRate() { return getRecentSuccessRate(); }
    }
}
