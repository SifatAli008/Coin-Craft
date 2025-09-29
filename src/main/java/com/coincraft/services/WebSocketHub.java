package com.coincraft.services;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketHub {
    private static WebSocketHub instance;
    private MessageServer server;
    private final Set<WebSocket> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private WebSocketClient client;
    private final String wsUrl = resolveWsUrl();

    public static synchronized WebSocketHub getInstance() {
        if (instance == null) instance = new WebSocketHub();
        return instance;
    }

    public void ensureServer() {
        if (server != null) return;
        // Only start local server if pointing to localhost
        if (wsUrl.startsWith("ws://127.0.0.1") || wsUrl.startsWith("ws://localhost")) {
            server = new MessageServer(new InetSocketAddress("127.0.0.1", 8123));
            server.setReuseAddr(true);
            try { server.start(); } catch (Exception ignored) {}
        }
    }

    public void connectClient() {
        if (client != null && client.isOpen()) return;
        try {
            client = new WebSocketClient(new URI(wsUrl)) {
                @Override public void onOpen(ServerHandshake handshakedata) {}
                @Override public void onMessage(String message) {
                    MessagingService.getInstance().dispatchWs(message);
                }
                @Override public void onClose(int code, String reason, boolean remote) {}
                @Override public void onError(Exception ex) {}
            };
            client.connect();
        } catch (Exception ignored) {}
    }

    // Send event string to server (simple JSON-like message)
    public void send(String payload) {
        connectClient();
        if (client != null && client.isOpen()) {
            client.send(payload);
        }
    }

    private class MessageServer extends WebSocketServer {
        public MessageServer(InetSocketAddress address) { super(address); }
        @Override public void onOpen(WebSocket conn, ClientHandshake handshake) { clients.add(conn); }
        @Override public void onClose(WebSocket conn, int code, String reason, boolean remote) { clients.remove(conn); }
        @Override public void onMessage(WebSocket conn, String message) {
            for (WebSocket c : clients) { try { c.send(message); } catch (Exception ignored) {} }
        }
        @Override public void onError(WebSocket conn, Exception ex) {}
        @Override public void onStart() {}
    }

    private String resolveWsUrl() {
        String env = System.getenv("COINCRAFT_WS_URL");
        String prop = System.getProperty("coincraft.ws.url");
        String url = env != null && !env.isEmpty() ? env : (prop != null && !prop.isEmpty() ? prop : "ws://127.0.0.1:8123");
        return url;
    }
}
