/*
 * Copyright 2018 Tic Tac Droid
 *
 */

package com.jomeno.tictactoe.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseTileActivity extends AppCompatActivity implements View.OnClickListener {

    private static String EXTRA_BOARD_SIZE = "board_size";
    private static String EXTRA_OPPONENT_PLAYER = "opponent_player";
    private static String EXTRA_TILE_CHOICE = "tile_choice";

    private int boardSize = 3;
    private boolean isOpponentAi = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tile);
        Intent intent = getIntent();
        boardSize = intent.getIntExtra(EXTRA_BOARD_SIZE, boardSize);
        isOpponentAi = intent.getBooleanExtra(EXTRA_OPPONENT_PLAYER, isOpponentAi);

        ImageView noughtMenuItem = findViewById(R.id.choice_nought);
        noughtMenuItem.setOnClickListener(this);
        ImageView crossMenuItem = findViewById(R.id.choice_cross);
        crossMenuItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        boolean tileChoice;
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(EXTRA_BOARD_SIZE, boardSize);
        intent.putExtra(EXTRA_OPPONENT_PLAYER, isOpponentAi);

        switch (view.getId()){
            case R.id.choice_nought:
                tileChoice = false;
                intent.putExtra(EXTRA_TILE_CHOICE, tileChoice);
                startActivity(intent);
                break;
            case R.id.choice_cross:
                tileChoice = true;
                intent.putExtra(EXTRA_TILE_CHOICE, tileChoice);
                startActivity(intent);
                break;
        }
    }
}
