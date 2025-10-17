package com.coincraft.game.adventure.models;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced NPC Manager with conversation system integration
 */
public class ConversationalNPCManager {
    private final List<ConversationalNPC> npcs;
    private final List<ConversationSystem> conversationSystems;
    
    public ConversationalNPCManager() {
        this.npcs = new ArrayList<>();
        this.conversationSystems = new ArrayList<>();
        System.out.println("🎭 Created Conversational NPC Manager");
    }
    
    /**
     * Create and add a Strong Adventurer NPC
     */
    public ConversationalNPC createAdventurer(String name, double x, double y) {
        ConversationSystem.DialogueNode conversationTree = NPCConversationTrees.createAdventurerConversation();
        ConversationSystem conversationSystem = new ConversationSystem(name, "Game Guide", conversationTree);
        conversationSystems.add(conversationSystem);
        
        ConversationalNPC npc = new ConversationalNPC(name, ConversationalNPC.NPCType.ADVENTURER, conversationSystem, x, y);
        npcs.add(npc);
        
        System.out.println("✅ Created Adventurer NPC: " + name + " at (" + x + ", " + y + ")");
        return npc;
    }
    
    /**
     * Create and add a Wise Lady NPC
     */
    public ConversationalNPC createSage(String name, double x, double y) {
        ConversationSystem.DialogueNode conversationTree = NPCConversationTrees.createWiseLadyConversation();
        ConversationSystem conversationSystem = new ConversationSystem(name, "Financial Sage", conversationTree);
        conversationSystems.add(conversationSystem);
        
        ConversationalNPC npc = new ConversationalNPC(name, ConversationalNPC.NPCType.SAGE, conversationSystem, x, y);
        npcs.add(npc);
        
        System.out.println("✅ Created Sage NPC: " + name + " at (" + x + ", " + y + ")");
        return npc;
    }
    
    /**
     * Create and add a Smart Businessman NPC
     */
    public ConversationalNPC createMerchant(String name, double x, double y) {
        ConversationSystem.DialogueNode conversationTree = NPCConversationTrees.createBusinessmanConversation();
        ConversationSystem conversationSystem = new ConversationSystem(name, "Business Expert", conversationTree);
        conversationSystems.add(conversationSystem);
        
        ConversationalNPC npc = new ConversationalNPC(name, ConversationalNPC.NPCType.MERCHANT, conversationSystem, x, y);
        npcs.add(npc);
        
        System.out.println("✅ Created Merchant NPC: " + name + " at (" + x + ", " + y + ")");
        return npc;
    }
    
    /**
     * Add an existing NPC to the manager
     */
    public void addNPC(ConversationalNPC npc) {
        npcs.add(npc);
        System.out.println("✅ Added NPC to manager: " + npc.getName());
    }
    
    /**
     * Remove an NPC from the manager
     */
    public void removeNPC(ConversationalNPC npc) {
        npcs.remove(npc);
        System.out.println("✅ Removed NPC from manager: " + npc.getName());
    }
    
    /**
     * Get all NPCs
     */
    public List<ConversationalNPC> getNPCs() {
        return new ArrayList<>(npcs);
    }
    
    /**
     * Get NPC count
     */
    public int getNPCCount() {
        return npcs.size();
    }
    
    /**
     * Find NPC by name
     */
    public ConversationalNPC findNPCByName(String name) {
        return npcs.stream()
                .filter(npc -> npc.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find NPCs by type
     */
    public List<ConversationalNPC> findNPCsByType(ConversationalNPC.NPCType type) {
        return npcs.stream()
                .filter(npc -> npc.getType() == type)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Update all NPCs
     */
    public void updateAll(double deltaTime) {
        for (ConversationalNPC npc : npcs) {
            npc.update(deltaTime);
        }
    }
    
    /**
     * Render all NPCs
     */
    public void renderAll(Pane parent) {
        for (ConversationalNPC npc : npcs) {
            npc.render(parent);
        }
    }
    
    /**
     * Set visibility for all NPCs
     */
    public void setAllVisible(boolean visible) {
        for (ConversationalNPC npc : npcs) {
            npc.setVisible(visible);
        }
    }
    
    /**
     * Set interactability for all NPCs
     */
    public void setAllInteractable(boolean interactable) {
        for (ConversationalNPC npc : npcs) {
            npc.setInteractable(interactable);
        }
    }
    
    /**
     * Find NPCs near a position
     */
    public List<ConversationalNPC> findNPCsNear(double x, double y, double radius) {
        List<ConversationalNPC> nearby = new ArrayList<>();
        for (ConversationalNPC npc : npcs) {
            double distance = Math.sqrt(
                Math.pow(x - npc.getX(), 2) + Math.pow(y - npc.getY(), 2)
            );
            if (distance <= radius) {
                nearby.add(npc);
            }
        }
        return nearby;
    }
    
    /**
     * Get the closest NPC to a position
     */
    public ConversationalNPC getClosestNPC(double x, double y) {
        ConversationalNPC closest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (ConversationalNPC npc : npcs) {
            double distance = Math.sqrt(
                Math.pow(x - npc.getX(), 2) + Math.pow(y - npc.getY(), 2)
            );
            if (distance < minDistance) {
                minDistance = distance;
                closest = npc;
            }
        }
        
        return closest;
    }
    
    /**
     * Start conversation with the closest NPC
     */
    public boolean startConversationWithClosest(double x, double y, double maxDistance) {
        ConversationalNPC closest = getClosestNPC(x, y);
        if (closest != null) {
            double distance = Math.sqrt(
                Math.pow(x - closest.getX(), 2) + Math.pow(y - closest.getY(), 2)
            );
            if (distance <= maxDistance && closest.isInteractable()) {
                closest.startConversation();
                return true;
            }
        }
        return false;
    }
    
    /**
     * End all active conversations
     */
    public void endAllConversations() {
        for (ConversationalNPC npc : npcs) {
            npc.endConversation();
        }
    }
    
    /**
     * Check if any NPC is currently in conversation
     */
    public boolean isAnyNPCInConversation() {
        return npcs.stream().anyMatch(ConversationalNPC::isInConversation);
    }
    
    /**
     * Clear all NPCs
     */
    public void clear() {
        endAllConversations();
        npcs.clear();
        conversationSystems.clear();
        System.out.println("✅ Cleared all NPCs from manager");
    }
}
