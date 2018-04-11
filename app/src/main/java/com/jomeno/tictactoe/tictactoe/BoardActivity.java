package com.jomeno.tictactoe.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Set> sets;
    private Set set;
    private ArrayList<Tile> tiles;
    private boolean NOUGHTS = false;
    private boolean CROSS = true;
    private int boardSize = 3;
    private Player turnPlayer;
    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        tiles = new ArrayList<>();
        sets = new ArrayList<>();
        set = new Set();

        // initialize players
        setupPlayers();

        // initialize board
        this.boardSize = 3;
        setupBoard(this.boardSize);
    }

    @Override
    public void onClick(View view) {

    }

    private void setupPlayers() {
        players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1);
        player1.setName("You");
        player1.setTileCross(false);
        players.add(player1);

        Player player2 = new Player();
        player2.setId(2);
        player2.setName("Player 2");
        player2.setTileCross(true);
        players.add(player2);

        // player 1 starts
        turnPlayer = player1;
    }

    private void setupBoard(final int boardSize) {

        tiles = new ArrayList<>();
        View boardLayout;
        LinearLayout boardLayoutEmpty = findViewById(R.id.board_tiles);
        if (boardSize == 5) {
            boardLayout = LayoutInflater.from(this).inflate(R.layout.layout_board_3, boardLayoutEmpty, false);
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
                    final ImageView imageTile = (ImageView) v;
                    final Tile tile = new Tile();

                    // set onclick listener
                    imageTile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // swap imageTile and toggle player
                            if (turnPlayer != null) {

                                if (set.isOver()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.game_over), Toast.LENGTH_SHORT);
                                    toast.show();
                                    return;
                                }
                                if (imageTile.getTag(imageTile.getId()) != null) return;

                                if (turnPlayer.isTileCross()) {
                                    imageTile.setImageResource(R.drawable.board_tile_cross);
                                } else {
                                    imageTile.setImageResource(R.drawable.board_tile_nought);
                                }

                                //Toast toast = Toast.makeText(getApplicationContext(), "Tapped", Toast.LENGTH_SHORT);
                                //toast.show();

                                // note that this tile has been taken
                                imageTile.setTag(imageTile.getId(), "TAKEN");
                                tile.setCross(turnPlayer.isTileCross());

                                // check if set over
                                setOutcome(boardSize);

                                // change player turn
                                if (turnPlayer.getId() == 1) {
                                    turnPlayer = players.get(1);
                                } else {
                                    turnPlayer = players.get(0);
                                }
                            }
                        }
                    });

                    tile.setImage(imageTile);
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
        if(matchCount == boardSize) {
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
        } else if (isWin == false) {
            message = getString(R.string.tie_message);
        }

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
