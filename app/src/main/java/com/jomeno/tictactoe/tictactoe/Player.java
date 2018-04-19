/**
 * Created by Jomeno on 4/9/2018.
 */

package com.jomeno.tictactoe.tictactoe;

public class Player {

    private boolean turn;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    public Boolean isTileCross() {
        return tileCross;
    }

    public void setTileCross(Boolean tileCross) {
        this.tileCross = tileCross;
    }

    private Boolean tileCross;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public  Player(){

    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
