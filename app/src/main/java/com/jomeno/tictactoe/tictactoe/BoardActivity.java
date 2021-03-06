/*
 * Copyright 2018 Tic Tac Droid
 *
 */

package com.jomeno.tictactoe.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Create and manages a Tic Tac board game.
 */
public class BoardActivity extends AppCompatActivity implements View.OnClickListener {

    private static String EXTRA_BOARD_SIZE = "board_size";
    private static String EXTRA_OPPONENT_PLAYER = "opponent_player";
    private static String EXTRA_TILE_CHOICE = "tile_choice";
    private static String TILES = "tiles";

    private ArrayList<Set> sets;
    private Set set;
    private ArrayList<Tile> tiles;
    private boolean NOUGHT = false;
    private boolean CROSS = true;

    private Player turnPlayer;
    private ArrayList<Player> players;
    private boolean isOpponentAi = true;
    private int boardSize = 3;
    private boolean player1TileChoice = NOUGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // recovering the instance state
        if (savedInstanceState != null) {
            boardSize = savedInstanceState.getInt(EXTRA_BOARD_SIZE);
            isOpponentAi = savedInstanceState.getBoolean(EXTRA_OPPONENT_PLAYER);
            player1TileChoice = savedInstanceState.getBoolean(EXTRA_TILE_CHOICE);
        }

        Intent intent = getIntent();
        if (intent != null) {
            boardSize = intent.getIntExtra(EXTRA_BOARD_SIZE, boardSize);
            isOpponentAi = intent.getBooleanExtra(EXTRA_OPPONENT_PLAYER, isOpponentAi);
            player1TileChoice = intent.getBooleanExtra(EXTRA_TILE_CHOICE, player1TileChoice);
        }

        tiles = new ArrayList<>();
        sets = new ArrayList<>();
        set = new Set();

        // initialize players
        setupPlayers();

        // initialize boards
        setupGameBoard(boardSize);
        setupScoreBoard();

        // register click listeners
        ImageView menuItem = findViewById(R.id.board_menu);
        menuItem.setOnClickListener(this);
        ImageView continueSet = findViewById(R.id.continue_set);
        continueSet.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_BOARD_SIZE, boardSize);
        outState.putBoolean(EXTRA_OPPONENT_PLAYER, isOpponentAi);
        outState.putBoolean(EXTRA_TILE_CHOICE, player1TileChoice);

        // save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private void setupScoreBoard() {
        ImageView player1ScoreTile = findViewById(R.id.player1_tile);
        ImageView player2ScoreTile = findViewById(R.id.player2_tile);
        if (players != null) {
            if (players.get(0).isTileCross()) {
                player1ScoreTile.setImageResource(R.drawable.white_cross_transparent);
                player2ScoreTile.setImageResource(R.drawable.white_naught_transparent);
            } else {
                player1ScoreTile.setImageResource(R.drawable.white_naught_transparent);
                player2ScoreTile.setImageResource(R.drawable.white_cross_transparent);
            }
        }
    }

    private void displayScoreBoard(boolean visible) {
        RelativeLayout scoreBoardRelativeLayout = findViewById(R.id.score_board);
        ImageView scoreBoardBgImageView = findViewById(R.id.score_board_bg);
        ImageView continueSet = findViewById(R.id.continue_set);
        if (visible) {
            scoreBoardRelativeLayout.setVisibility(View.VISIBLE);
            scoreBoardBgImageView.setVisibility(View.VISIBLE);
            continueSet.setVisibility(View.VISIBLE);
        } else {
            scoreBoardRelativeLayout.setVisibility(View.INVISIBLE);
            scoreBoardBgImageView.setVisibility(View.INVISIBLE);
            continueSet.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.board_menu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.continue_set:
                beginNewSet(view);
                break;
        }

    }

    private void beginNewSet(View view) {
        if (set.isOver()) {
            set = new Set();
            turnPlayer = players.get(0);
            setupGameBoard(boardSize);
            displayScoreBoard(false);
        }
    }

    private boolean play(Tile tile, ImageView tileImage) {
        if (set.isOver()) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.game_over), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        //if (tileImage.getTag(tileImage.getId()) != null) {
        if (tile.isTaken() == true) {
            Toast toast = Toast.makeText(getApplicationContext(), "This tile has been taken", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (turnPlayer.isTileCross()) {
            tileImage.setImageResource(R.drawable.board_tile_cross);
        } else {
            tileImage.setImageResource(R.drawable.board_tile_nought);
        }

        /*int id = tiles.indexOf(tile);
        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT);
        toast.show();*/

        // mark tileImage and tile as taken
        //tileImage.setTag(tileImage.getId(), "TAKEN");
        tile.setTaken(true);
        tile.setCross(turnPlayer.isTileCross());

        // check if set is over
        setOutcome(boardSize);

        return true;
    }

    private void setupPlayers() {
        players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1);
        player1.setName("You");
        player1.setTileCross(player1TileChoice);
        players.add(player1);

        Player player2 = new Player();
        player2.setId(2);
        player2.setName("Player 2");
        player2.setTileCross(!player1TileChoice);
        players.add(player2);

        // player 1 starts
        turnPlayer = player1;
    }

    private void setupGameBoard(final int boardSize) {

        tiles = new ArrayList<>();
        View boardLayout;
        LinearLayout boardLayoutEmpty = findViewById(R.id.board_tiles);
        boardLayoutEmpty.removeAllViews();
        if (boardSize == 5) {
            boardLayout = LayoutInflater.from(this).inflate(R.layout.layout_board_5, boardLayoutEmpty, false);
        } else {
            boardLayout = LayoutInflater.from(this).inflate(R.layout.layout_board_3, boardLayoutEmpty, false);
        }

        // setup click listeners
        final int rowCount = ((LinearLayout) boardLayout).getChildCount();
        for (int i = 0; i < rowCount; i++) {
            View rowLayout = ((LinearLayout) boardLayout).getChildAt(i);

            final int imageCount = ((LinearLayout) rowLayout).getChildCount();

            for (int j = 0; j < imageCount; j++) {
                View v = ((LinearLayout) rowLayout).getChildAt(j);
                if (v instanceof ImageView) {
                    final ImageView tileImage = (ImageView) v;
                    final Tile tile = new Tile();

                    // set onclick listener
                    tileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // swap tileImage and toggle player

                            if (turnPlayer != null) {

                                // play turn
                                boolean isPlayed = play(tile, tileImage);

                                // set next player
                                if (isPlayed) {
                                    if (turnPlayer.getId() == 1) {
                                        turnPlayer = players.get(1);
                                    } else {
                                        turnPlayer = players.get(0);
                                    }
                                }

                                // play for AI
                                if (turnPlayer.getId() == 2 && isOpponentAi && !set.isOver()) {
                                    ArrayList<Tile> unusedTiles = new ArrayList<>();
                                    for (Tile t : tiles) {
                                        if (t.isTaken() == false) unusedTiles.add(t);
                                    }

                                    if (unusedTiles.isEmpty() == false) {
                                        Random rand = new Random();
                                        int tileIndex = rand.nextInt(unusedTiles.size());
                                        Tile aiTile = unusedTiles.get(tileIndex);
                                        play(aiTile, aiTile.getImage());
                                        // set next player
                                        turnPlayer = players.get(0);
                                    }
                                }
                            }
                        }
                    });

                    tile.setImage(tileImage);
                    tiles.add(tile);
                }
            }
        }
        boardLayoutEmpty.addView(boardLayout);
    }

    private void setOutcome(int boardSize) {

        boolean leftDiagonalWin = false;
        int matchCount = 0;

        // check for a horizontal win
        matchCount = horizontalMatchCount(boardSize);
        if (matchCount == boardSize) {
            gameOver(true, matchCount, boardSize);
            return;
        }

        // check for a vertical win
        matchCount = verticalMatchCount(boardSize);
        if (matchCount == boardSize) {
            gameOver(true, matchCount, boardSize);
            return;
        }

        // check for a left diagonal win
        matchCount = leftDiagonalMatchCount(boardSize);
        if (matchCount == boardSize) {
            gameOver(true, matchCount, boardSize);
            return;
        }

        // check for a right diagonal win
        matchCount = rightDiagonalMatchCount(boardSize);
        if (matchCount == boardSize) {
            gameOver(true, matchCount, boardSize);
            return;
        }

        // check for a tie
        boolean isTileEmpty = false;
        for (Tile tile : tiles) {
            isTileEmpty = tile.isCross() == null;
            if (isTileEmpty) break;
        }

        if (isTileEmpty == false) {
            gameOver(false, 0, boardSize);
        }

    }

    // horizontal win check
    private int horizontalMatchCount(int boardSize) {
        int matchCount = 0;
        boolean horizontalWin;
        for (int i = 0; i < this.tiles.size(); i++) {
            if (i % boardSize == 0) {
                matchCount = 0;
                horizontalWin = false;
            }

            Tile tile = this.tiles.get(i);
            if (tile.isCross() != null) {
                horizontalWin = turnPlayer.isTileCross() == tile.isCross();
                if (horizontalWin) matchCount++;
            } else {
                matchCount = 0;
                horizontalWin = false;
            }

            if (matchCount == boardSize) {
                return matchCount;
            }
        }
        return matchCount;
    }

    // vertical win check
    private int verticalMatchCount(int boardSize) {
        int matchCount = 0;
        boolean verticalWin;
        for (int i = 0; i < boardSize; i++) {
            matchCount = 0;
            verticalWin = false;

            for (int j = i; j < this.tiles.size(); j += boardSize) {
                Tile tile = this.tiles.get(j);
                if (tile.isCross() != null) {
                    verticalWin = turnPlayer.isTileCross() == tile.isCross();
                    if (verticalWin) matchCount++;
                } else {
                    matchCount = 0;
                    verticalWin = false;
                }
            }

            if (matchCount == boardSize) {
                return matchCount;
            }
        }
        return matchCount;
    }

    // diagonal win check
    private int leftDiagonalMatchCount(int boardSize) {
        boolean diagonalWin;
        int matchCount;
        matchCount = 0;
        for (int i = 0; i < boardSize; i++) {
            Tile tile = this.tiles.get(i + (i * boardSize));
            if (tile.isCross() != null) {
                diagonalWin = turnPlayer.isTileCross() == tile.isCross();
                if (diagonalWin) matchCount++;
            } else {
                matchCount = 0;
                diagonalWin = false;
            }

            if (matchCount == boardSize) {
                return matchCount;
            }
        }
        return matchCount;
    }

    // diagonal win check
    private int rightDiagonalMatchCount(int boardSize) {
        boolean diagonalWin;
        int matchCount;
        matchCount = 0;
        for (int i = 0; i < boardSize; i++) {
            Tile tile = this.tiles.get((boardSize - 1) + (i * (boardSize - 1)));
            if (tile.isCross() != null) {
                diagonalWin = turnPlayer.isTileCross() == tile.isCross();
                if (diagonalWin) matchCount++;
            } else {
                matchCount = 0;
                diagonalWin = false;
            }

            if (matchCount == boardSize) {
                return matchCount;
            }
        }
        return matchCount;
    }

    private void gameOver(boolean isWin, int winCount, int boardSize) {
        this.set.setOver(true);
        String message = getString(R.string.game_over);

        if (isWin && (winCount == boardSize)) {
            message = getString(R.string.winner, turnPlayer.getName());
            // update player score
            turnPlayer.setScore(turnPlayer.getScore() + 1);

            TextView player1ScoreTextView = findViewById(R.id.player1_score);
            Player player1 = this.players.get(0);
            player1ScoreTextView.setText(String.valueOf(player1.getScore()));

            TextView player2ScoreTextView = findViewById(R.id.player2_score);
            Player player2 = this.players.get(1);
            player2ScoreTextView.setText(String.valueOf(player2.getScore()));

        } else if (isWin == false) {
            message = getString(R.string.tie_message);
        }

        // archive set
        this.sets.add(set);

        displayScoreBoard(true);

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
