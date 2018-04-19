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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String EXTRA_BOARD_SIZE = "board_size";
    private static String EXTRA_OPPONENT_PLAYER = "opponent_player";

    private boolean isOpponentAi = true;
    private int boardSize = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView newGameMenuItem = findViewById(R.id.new_game);
        newGameMenuItem.setOnClickListener(this);
        ImageView playFriendMenuItem = findViewById(R.id.play_a_friend);
        playFriendMenuItem.setOnClickListener(this);
        ImageView toggleBoardMenuItem = findViewById(R.id.toggle_board);
        toggleBoardMenuItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_game:
                Intent intent = new Intent(this, ChooseTileActivity.class);
                intent.putExtra(EXTRA_BOARD_SIZE, boardSize);
                intent.putExtra(EXTRA_OPPONENT_PLAYER, isOpponentAi);
                startActivity(intent);
                break;
            case R.id.play_a_friend:
                isOpponentAi = !isOpponentAi;
                String message;
                ImageView friendMenuImage = findViewById(R.id.play_a_friend);
                if(isOpponentAi){
                    message = getString(R.string.play_ai);
                    friendMenuImage.setImageResource(R.drawable.menu_play_a_friend);
                }else{
                    message = getString(R.string.play_friend);
                    friendMenuImage.setImageResource(R.drawable.menu_play_ai);
                }
                Toast playFriend = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                playFriend.show();
                break;
            case R.id.toggle_board:
                String boardMessage;
                if(boardSize == 3){
                    boardSize = 5;
                    boardMessage = getString(R.string.board_large);
                }else{
                    boardSize = 3;
                    boardMessage = getString(R.string.board_small);
                }
                Toast boardToggleToast = Toast.makeText(this, boardMessage, Toast.LENGTH_SHORT);
                boardToggleToast.show();
                break;
        }
    }
}
