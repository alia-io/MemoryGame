package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

// Third Activity - Game Over & Display Score (number of card flips)
public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int score = getIntent().getIntExtra("score", 0);
        TextView textView = findViewById(R.id.score_text);

        if (score < 12) {
            textView.setText(R.string.error_text);
        } else {
            String text = getString(R.string.score_text) + " " + score;
            textView.setText(text);
        }
    }

    public void playAgain(View view) {
        Intent intent = new Intent(this, ChooseDifficultyActivity.class);
        startActivity(intent);
    }
}