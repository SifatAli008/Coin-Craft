package com.coincraft.engine;

import javafx.animation.AnimationTimer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Minimal game loop that updates registered systems and renderers.
 */
public class GameLoop {
    private final List<Updatable> updatables = new CopyOnWriteArrayList<>();
    private final List<Runnable> renderers = new CopyOnWriteArrayList<>();
    
    private final AnimationTimer timer;
    private long lastTime = 0;
    
    public GameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double delta = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                
                for (Updatable u : updatables) {
                    u.update(delta);
                }
                for (Runnable r : renderers) {
                    r.run();
                }
            }
        };
    }
    
    public void start() { timer.start(); }
    public void stop() { timer.stop(); }
    
    public void addUpdatable(Updatable u) { if (u != null) updatables.add(u); }
    public void removeUpdatable(Updatable u) { updatables.remove(u); }
    
    public void addRenderer(Runnable r) { if (r != null) renderers.add(r); }
    public void removeRenderer(Runnable r) { renderers.remove(r); }
}


