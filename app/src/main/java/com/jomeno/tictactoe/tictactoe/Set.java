package com.jomeno.tictactoe.tictactoe;

import java.util.ArrayList;

/**
 * Created by Jomeno on 4/9/2018.
 */

public class Set {

    public Set() {
        this.over = false;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    private boolean over;

    private ArrayList<Player> players;
}
