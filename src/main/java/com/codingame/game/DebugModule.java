package com.codingame.game;

import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.core.SoloGameManager;
import com.google.inject.Inject;

import java.util.ArrayList;

public class DebugModule implements Module {

    private final ArrayList<Integer> debugItems = new ArrayList<>();
    private final ArrayList<Integer> newItems = new ArrayList<>();
    SoloGameManager<Player> gameManager;

    @Inject
    public DebugModule(SoloGameManager<Player> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
    }

    @Override
    public void onGameInit() {
        sendItems();
    }

    @Override
    public void onAfterGameTurn() {
        sendItems();
    }

    @Override
    public void onAfterOnEnd() {
        sendItems();
    }

    private void sendItems() {
        gameManager.setViewData("debug", newItems.toArray());
        debugItems.addAll(newItems);
        newItems.clear();
    }

    public void addItem(int id) {
        if (debugItems.contains(id)) return;
        newItems.add(id);
    }
}