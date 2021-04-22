package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainGameActivity extends AppCompatActivity {

    private int score = 0;
    private int difficulty;
    private GridLayout grid;
    private int lastFlippedCardIndex = -1;

    // Integer Id = R.drawable.name, Integer NumberAvailable
    HashMap<Integer, Integer> cardResources = new HashMap<Integer, Integer>(16);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        // Set the difficulty
        difficulty = getIntent().getIntExtra("difficulty", 1);

        // Get the IDs of the face-up card drawable resources
        setCardResources();

        // Get the screen size available
        ViewGroup view = findViewById(R.id.main_container);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    initializeGrid(view.getWidth(), view.getHeight());
                }
            });
        }
    }

    private void setCardResources() {
        ArrayList<Integer> cardResourceIds = new ArrayList<Integer>(16);

        // Add all the face-up cards
        cardResourceIds.add(R.drawable.card_0);
        cardResourceIds.add(R.drawable.card_1);
        cardResourceIds.add(R.drawable.card_2);
        cardResourceIds.add(R.drawable.card_3);
        cardResourceIds.add(R.drawable.card_4);
        cardResourceIds.add(R.drawable.card_5);
        cardResourceIds.add(R.drawable.card_6);
        cardResourceIds.add(R.drawable.card_7);
        cardResourceIds.add(R.drawable.card_8);
        cardResourceIds.add(R.drawable.card_9);
        cardResourceIds.add(R.drawable.card_10);
        cardResourceIds.add(R.drawable.card_11);
        cardResourceIds.add(R.drawable.card_12);
        cardResourceIds.add(R.drawable.card_13);
        cardResourceIds.add(R.drawable.card_14);
        //cardResourceIds.add(R.drawable.card_15);

        for (int id : cardResourceIds) cardResources.put(id, 2);

        if (difficulty == 3) { // difficult (32 cards / 16 pairs)
            return; // Keep all 16 pairs
        } else if (difficulty == 2) { // medium (20 cards / 10 pairs)
            // Remove 6 pairs

        } else { // easy (12 cards / 6 pairs)
            // Remove 10 pairs

        }
    }

    private void initializeGrid(int viewWidth, int viewHeight) {

        grid = findViewById(R.id.main_grid);
        int side;

        if (viewWidth < viewHeight) side = viewWidth;
        else side = viewHeight;

        if (difficulty == 3) { // difficult (4x8)
            int cardSide = (int) Math.floor(side * 0.2);
            int padding = (int) Math.floor(side * 0.02);
            grid.setColumnCount(4);
            for (int i = 0; i < 32; i++) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.card_9);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(cardSide, cardSide));
                grid.addView(imageView);
                //Card card = new Card();
            }
        } else if (difficulty == 2) { // medium (4x5)
            int cardSide = (int) Math.floor(side * 0.2);
            int padding = (int) Math.floor(side * 0.02);
            grid.setColumnCount(4);
            for (int i = 0; i < 20; i++) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.empty_card_slot);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(cardSide, cardSide));
                grid.addView(imageView);
                //Card card = new Card();
            }
        } else { // easy (3x4)
            int cardSide = (int) Math.floor(side * 0.24);
            int padding = (int) Math.floor(side * 0.02);
            grid.setColumnCount(3);
            for (int i = 0; i < 12; i++) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.card_back);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(cardSide, cardSide));
                grid.addView(imageView);
                //Card card = new Card();
            }
        }
    }

    private class Card {
        private ImageView image;
        private boolean matched;
        private int cardNumber;
        private int layoutIndex;

        private Card(ImageView image, int cardNumber, int layoutIndex) {
            this.image = image;
            this.cardNumber = cardNumber;
        }
    }
}