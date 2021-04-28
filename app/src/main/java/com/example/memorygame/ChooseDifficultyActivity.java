package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// First Activity - Choose Easy, Medium, or Difficult
public class ChooseDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
    }

    public void play(View view) {
        Intent intent = new Intent(this, MainGameActivity.class);

        if (view.getId() == R.id.difficult) intent.putExtra("difficulty", 3);
        else if (view.getId() == R.id.medium) intent.putExtra("difficulty", 2);
        else intent.putExtra("difficulty", 1);

        startActivity(intent);
    }
}