package io.jiayou.hanzi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] hanzi_list;
    private int[] score_list;
    int currIndex = 0;
    boolean skipEnable = true;

    TextView textDisplay;
    RatingBar scoreBar;

    private float x1, x2;
    private float y1, y2;
    static final int MIN_X_DISTANCE = 150;
    static final int MIN_Y_DISTANCE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hanzi_list = getResources().getStringArray(R.array.hanzi_list);

        // find first unknown character

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        score_list = new int[hanzi_list.length];
        for (int i = 0; i < hanzi_list.length; i++) {
            String x = hanzi_list[i];
            score_list[i] = sharedPref.getInt(x, 0);
        }

        textDisplay = (TextView) findViewById(R.id.texthanzi);
        textDisplay.setText(hanzi_list[currIndex]);

        scoreBar = (RatingBar) findViewById(R.id.ratingBar);
        scoreBar.setStepSize(1);
        scoreBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                score_list[currIndex] = (int) rating;
            }
        });
        scoreBar.setRating(score_list[currIndex]);
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < hanzi_list.length; i++) {
            editor.putInt(hanzi_list[i], score_list[i]);
        }
        editor.commit();

        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                if (Math.abs(deltaX) > MIN_X_DISTANCE ) {
                    if (x2 < x1) {
                        goNextChar();
                    }
                    else {
                        goPrevChar();
                    }
                }
                if (Math.abs(deltaY) > MIN_Y_DISTANCE ) {
                    if (y2 > y1) {
                        scoreDecrease();
                    } else {
                        scoreIncrease();
                    }
                } else {
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    void goNextChar() {

        do {
            currIndex++;
            if (currIndex >= hanzi_list.length) {
                currIndex = 0;
            }
        }
        while (score_list[currIndex] >= 5 && skipEnable);

        // TODO: if all characters are skipped, the endless loop must be terminated.

        textDisplay.setText(hanzi_list[currIndex]);
        scoreBar.setRating(score_list[currIndex]);
    }


    void goPrevChar() {

        do {
            currIndex--;
            if (currIndex <= 0) {
                currIndex = hanzi_list.length - 1;
            }
        }
        while (score_list[currIndex] >= 5 && skipEnable);

        // TODO: if all characters are skipped, the endless loop must be terminated.

        textDisplay.setText(hanzi_list[currIndex]);
        scoreBar.setRating(score_list[currIndex]);
    }

    void scoreIncrease() {
        score_list[currIndex]++;
        if (score_list[currIndex] >= 5) {
            score_list[currIndex] = 5;
        }
        scoreBar.setRating(score_list[currIndex]);
    }

    void scoreDecrease() {
        score_list[currIndex]--;
        if (score_list[currIndex] < 0) {
            score_list[currIndex] = 0;
        }
        scoreBar.setRating(score_list[currIndex]);
    }
}
