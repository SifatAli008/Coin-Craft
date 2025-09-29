package com.coincraft.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import com.coincraft.models.MessageData;

/**
 * Messaging service that persists messages via FirebaseService and provides
 * a lightweight polling listener to simulate realtime updates when websockets
 * are unavailable. Replace polling with Firestore SDK listeners if available.
 */
public class MessagingService {
    private static MessagingService instance;
    private final FirebaseService firebase;
    private final List<Listener> listeners;
    private final Timer timer;

    private MessagingService() {
        this.firebase = FirebaseService.getInstance();
        this.listeners = new CopyOnWriteArrayList<>();
        this.timer = new Timer("MessagingPoller", true);
        startPolling();
        // Ensure local WS hub for push within same machine
        try { WebSocketHub.getInstance().ensureServer(); WebSocketHub.getInstance().connectClient(); } catch (Exception ignored) {}
    }

    public static MessagingService getInstance() {
        if (instance == null) instance = new MessagingService();
        return instance;
    }

    public boolean sendMessage(String conversationId, String senderId, String senderName,
                               String recipientId, String recipientName, String content) {
        MessageData m = new MessageData();
        m.setMessageId("msg_" + System.currentTimeMillis());
        m.setConversationId(conversationId);
        m.setSenderId(senderId);
        m.setSenderName(senderName);
        m.setRecipientId(recipientId);
        m.setRecipientName(recipientName);
        m.setContent(content);
        m.setTimestamp(LocalDateTime.now());
        boolean ok = firebase.saveMessage(m);
        if (ok) {
            // Notify via local websocket for immediate UI updates across windows
            String payload = conversationId + "|" + (m.getTimestamp() != null ? m.getTimestamp().toString() : "") ;
            WebSocketHub.getInstance().send(payload);
        }
        return ok;
    }

    public List<MessageData> getRecent(String conversationId, int limit) {
        return firebase.loadConversation(conversationId, limit);
    }

    public void addListener(String conversationId, Listener listener) {
        listener.conversationId = conversationId;
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void startPolling() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                for (Listener l : new ArrayList<>(listeners)) {
                    try {
                        List<MessageData> latest = firebase.loadConversation(l.conversationId, 100);
                        if (latest == null) latest = List.of();
                        // Only notify if newer than last timestamp
                        if (!latest.isEmpty()) {
                            LocalDateTime newest = latest.get(latest.size()-1).getTimestamp();
                            if (l.lastNotified == null || (newest != null && newest.isAfter(l.lastNotified))) {
                                l.lastNotified = newest;
                                l.onUpdate(latest);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        }, 0, 2000); // 2s polling
    }

    // Called by WebSocketHub when a message arrives
    public void dispatchWs(String payload) {
        // payload: conversationId|timestamp
        String conversationId = payload != null && payload.contains("|") ? payload.split("\\|",2)[0] : payload;
        if (conversationId == null) return;
        for (Listener l : new ArrayList<>(listeners)) {
            if (conversationId.equals(l.conversationId)) {
                List<MessageData> latest = firebase.loadConversation(conversationId, 100);
                l.onUpdate(latest);
            }
        }
    }

    public abstract static class Listener {
        private String conversationId;
        private LocalDateTime lastNotified;
        public abstract void onUpdate(List<MessageData> messages);
    }
}


