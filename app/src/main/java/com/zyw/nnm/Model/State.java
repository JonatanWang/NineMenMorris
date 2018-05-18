package com.zyw.nnm.Model;

import java.io.Serializable;

// ModelState to copy model for purpose of saving and loading
public class State implements Serializable {

    private NineMenMorrisRules gameState;

    public State(NineMenMorrisRules gameState) {
        this.gameState = gameState;
    }

    public NineMenMorrisRules getGameState() {
        return gameState;
    }
}

