package com.jomeno.tictactoe.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView newGameMenuItem = findViewById(R.id.new_game);
        newGameMenuItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_game:
                Intent intent = new Intent(this, BoardActivity.class);
                startActivity(intent);
                break;
        }
    }
}
