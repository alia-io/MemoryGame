package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainGameActivity extends AppCompatActivity {

    private int score = 0;
    private int numberOfCardsLeft = 0;
    private int difficulty;
    private GridLayout grid;
    private int lastFlippedCardIndex = -1;

    // Integer Id = R.drawable.name, Integer NumberAvailable
    ArrayList<CardResource> cardResources = new ArrayList<CardResource>(16);
    ArrayList<Card> cardsOnBoard = new ArrayList<Card>(32);

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
        // Add all the face-up cards
        cardResources.add(new CardResource(0, R.drawable.card_0));
        cardResources.add(new CardResource(1, R.drawable.card_1));
        cardResources.add(new CardResource(2, R.drawable.card_2));
        cardResources.add(new CardResource(3, R.drawable.card_3));
        cardResources.add(new CardResource(4, R.drawable.card_4));
        cardResources.add(new CardResource(5, R.drawable.card_5));
        cardResources.add(new CardResource(6, R.drawable.card_6));
        cardResources.add(new CardResource(7, R.drawable.card_7));
        cardResources.add(new CardResource(8, R.drawable.card_8));
        cardResources.add(new CardResource(9, R.drawable.card_9));
        cardResources.add(new CardResource(10, R.drawable.card_10));
        cardResources.add(new CardResource(11, R.drawable.card_11));
        cardResources.add(new CardResource(12, R.drawable.card_12));
        cardResources.add(new CardResource(13, R.drawable.card_13));
        cardResources.add(new CardResource(14, R.drawable.card_14));
        cardResources.add(new CardResource(15, R.drawable.card_15));

        if (difficulty == 2) { // medium (20 cards / 10 pairs)
            for (int i = 0; i < 6; i++) { // Remove 6 resources
                cardResources.remove((int) (Math.random() * cardResources.size()));
            }
        } else if (difficulty == 1) { // easy (12 cards / 6 pairs)
            for (int i = 0; i < 10; i++) { // Remove 10 resources
                cardResources.remove((int) (Math.random() * cardResources.size()));
            }
        }
    }

    private void initializeGrid(int viewWidth, int viewHeight) {
        int cardSide = 0;
        int side;
        int padding;

        if (viewWidth < viewHeight) side = viewWidth;
        else side = viewHeight;

        padding = (int) Math.floor(side * 0.02);
        grid = findViewById(R.id.main_grid);

        if (difficulty == 3) { // difficult (4x8)
            cardSide = (int) Math.floor(side * 0.2);
            numberOfCardsLeft = 32;
            grid.setColumnCount(4);
        } else if (difficulty == 2) { // medium (4x5)
            cardSide = (int) Math.floor(side * 0.2);
            numberOfCardsLeft = 20;
            grid.setColumnCount(4);
        } else { // easy (3x4)
            cardSide = (int) Math.floor(side * 0.24);
            numberOfCardsLeft = 12;
            grid.setColumnCount(3);
        }

        for (int i = 0; i < numberOfCardsLeft; i++) {
            int random = (int) (Math.random() * cardResources.size());
            CardResource cardResource = cardResources.get(random);
            cardResource.numberAvailable--;
            if (cardResource.numberAvailable <= 0) cardResources.remove(random);
            new Card(cardResource.drawableId, cardResource.cardNumber, i, cardSide, padding);
        }
    }

    private class CardResource {
        private int cardNumber;
        private int drawableId;
        private int numberAvailable;

        private CardResource(int cardNumber, int drawableId) {
            this.cardNumber = cardNumber;
            this.drawableId = drawableId;
            numberAvailable = 2;
        }
    }

    private class Card {
        private ImageButton button;
        private boolean matched;
        private int drawableId;
        private int cardNumber;
        private int layoutIndex;

        private Card(int drawableId, int cardNumber, int layoutIndex, int sideLength, int paddingLength) {
            Log.d("Card", "Id = " + drawableId + ", number = " + cardNumber + ", index = " + layoutIndex
                + ", side = " + sideLength + ", padding = " + paddingLength);

            this.drawableId = drawableId;
            this.cardNumber = cardNumber;
            this.layoutIndex = layoutIndex;
            matched = false;

            button = new ImageButton(getApplicationContext());
            button.setImageResource(R.drawable.card_back);
            button.setAdjustViewBounds(true);
            button.setBackground(null);
            button.setPadding(paddingLength, paddingLength, paddingLength, paddingLength);
            button.setLayoutParams(new LinearLayout.LayoutParams(sideLength, sideLength));

            button.setOnClickListener(view -> {
                Log.d("Card", "Card #" + layoutIndex + " Clicked");
                // TODO: card button action on click
            });

            grid.addView(button);
        }
    }
}