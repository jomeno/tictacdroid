/**
 * Created by Jomeno on 4/9/2018.
 */

package com.jomeno.tictactoe.tictactoe;

import android.widget.ImageView;

public class Tile {

    public Tile() {
        isCross = null;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    private ImageView image;

    public Boolean isCross() {
        return isCross;
    }

    public void setCross(Boolean cross) {
        isCross = cross;
    }

    private Boolean isCross;

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    private boolean isTaken;
}
