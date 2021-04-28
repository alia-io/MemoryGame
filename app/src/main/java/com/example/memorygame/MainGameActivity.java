package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// Second Activity - Memory Game
public class MainGameActivity extends AppCompatActivity {

    private int score = 0;
    private int difficulty;
    private GridLayout grid;
    private int lastFlippedCardIndex = -1;
    private boolean allowCardFlips = false;
    Timer timer = new Timer();

    // List of available card resources (drawable ID, card number, number available)
    ArrayList<CardResource> cardResources = new ArrayList<CardResource>(16);

    // List of cards in the current game
    ArrayList<Card> cardsOnBoard = new ArrayList<Card>(32);
    int numberOfCards = 0; // Number of cards left to clear

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
                    initializeGrid(view.getWidth(), view.getHeight()); // Set card sizes based on available screen size
                }
            });
        }

        allowCardFlips = true; // Allow player actions to flip cards
    }

    /* Adds the appropriate number of card/image resources to cardResources list based on game difficulty */
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

    /* Initializes card objects and their locations, adds card images face-down to the grid */
    private void initializeGrid(int viewWidth, int viewHeight) {
        int cardSide;
        int side;
        int padding;

        if (viewWidth < viewHeight) side = viewWidth;
        else side = viewHeight;

        padding = (int) Math.floor(side * 0.02);
        grid = findViewById(R.id.main_grid);

        if (difficulty == 3) { // difficult (4x8)
            cardSide = (int) Math.floor(side * 0.2);
            numberOfCards = 32;
            grid.setColumnCount(4);
        } else if (difficulty == 2) { // medium (4x5)
            cardSide = (int) Math.floor(side * 0.2);
            numberOfCards = 20;
            grid.setColumnCount(4);
        } else { // easy (3x4)
            cardSide = (int) Math.floor(side * 0.24);
            numberOfCards = 12;
            grid.setColumnCount(3);
        }

        for (int i = 0; i < numberOfCards; i++) {
            int random = (int) (Math.random() * cardResources.size());
            CardResource cardResource = cardResources.get(random); // Get a random resource from the pool
            // Initialize new card; it is added to the grid through initialization
            Card card = new Card(cardResource.drawableId, cardResource.cardNumber, i, cardSide, padding);
            cardResource.numberAvailable--;
            if (cardResource.numberAvailable > 0) {
                cardResource.firstCardIndex = i;
            } else {    // Set the indices of the cards' matches
                int index = cardResource.firstCardIndex;
                cardsOnBoard.get(index).matchIndex = i;
                card.matchIndex = index;
                cardResources.remove(random);
            }
            cardsOnBoard.add(card);
        }
    }

    /* Data associated with a card resource: card number, drawable ID, number available, index of its match */
    private class CardResource {
        private int cardNumber;
        private int drawableId;
        private int numberAvailable;
        private int firstCardIndex;

        private CardResource(int cardNumber, int drawableId) {
            this.cardNumber = cardNumber;
            this.drawableId = drawableId;
            numberAvailable = 2;
        }
    }

    /* Data associated with each card on the grid */
    private class Card {
        private ImageButton button;
        private boolean matched;
        private int drawableId;
        private int cardNumber;
        private int layoutIndex;
        private int matchIndex;

        /* Sets Card fields and renders card image in GUI */
        private Card(int drawableId, int cardNumber, int layoutIndex, int sideLength, int paddingLength) {
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
            button.setOnClickListener(view -> flipCard(this));
            grid.addView(button);
        }
    }

    /* Actions to take when a card is flipped */
    private synchronized void flipCard(Card card) {
        if (allowCardFlips && !card.matched && lastFlippedCardIndex != card.layoutIndex) {
            allowCardFlips = false;
            score++;
            card.button.setImageResource(card.drawableId); // Set card image to face-up in GUI
            if (lastFlippedCardIndex == -1) { // First card in pair flipped
                firstInPairFlipped(card);
            } else if (lastFlippedCardIndex == card.matchIndex) { // Matching pair found
                matchingCardFlipped(card);
            } else { // Non-matching pair
                notMatchingCardFlipped(card);
            }
        }
    }

    /* First card in pair flipped */
    private void firstInPairFlipped(Card card) {
        // Set timer for slight delay before allowing player to flip another (prevent simultaneous flips)
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                allowCardFlips = true;
            }
        }, 10);
        lastFlippedCardIndex = card.layoutIndex;
    }

    /* Second card flipped and a match was found */
    private void matchingCardFlipped(Card card) {
        timer.schedule(new TimerTask() { // First timer - delay removing card so player can see match, then display "matched" images
            @Override
            public void run() {
                Card otherCard = cardsOnBoard.get(lastFlippedCardIndex);
                numberOfCards -= 2;
                card.matched = true;
                otherCard.matched = true;
                card.button.setImageResource(R.drawable.card_match);
                otherCard.button.setImageResource(R.drawable.card_match);
                lastFlippedCardIndex = -1;
                timer.schedule(new TimerTask() { // Second timer - remove "matched" images and set empty slots
                    @Override
                    public void run() {
                        card.button.setImageResource(R.drawable.empty_card_slot);
                        otherCard.button.setImageResource(R.drawable.empty_card_slot);
                    }
                }, 200);
                if (numberOfCards <= 0) { // All matches have been found
                    gameOver();
                } else {
                    allowCardFlips = true;
                }
            }
        }, 300);
    }

    /* Second card flipped and does not match first card */
    private void notMatchingCardFlipped(Card card) {
        // Set timer for delay to allow player to memorize card images
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Card otherCard = cardsOnBoard.get(lastFlippedCardIndex);
                card.button.setImageResource(R.drawable.card_back);
                otherCard.button.setImageResource(R.drawable.card_back);
                lastFlippedCardIndex = -1;
                allowCardFlips = true;
            }
        }, 500);
    }

    /* All cards cleared - end the game by going to next activity */
    private void gameOver() {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}