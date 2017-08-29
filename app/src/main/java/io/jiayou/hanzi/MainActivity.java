package io.jiayou.hanzi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] hanzi_list;
    private int[] score_list;
    private float x1,x2;
    private float y1,y2;
    static final int MIN_DISTANCE = 150;
    static final int MAX_DISTANCE = 50;
    TextView textDisplay;

    String currChar;
    int currIndex = 0;
    int currScore = 0;
    int maxIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hanzi_list = getResources().getStringArray(R.array.hanzi_list);

        // find first unknown character

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        score_list = new int[hanzi_list.length];
        for (int i=0; i<hanzi_list.length; i++)
        {
            String x = hanzi_list[i];
            score_list[i] = sharedPref.getInt(x, 5);
        }



        TextView textView = (TextView)findViewById(R.id.texthanzi);
        textDisplay = textView;

        currChar = hanzi_list[currIndex];
        textView.setText(currChar);

    }

    @Override
    protected void onDestroy() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i=0; i<hanzi_list.length; i++) {
            editor.putInt(hanzi_list[i], score_list[i]);
        }
        editor.commit();

        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MAX_DISTANCE)
                {
                    if (x2 < x1)
                    {
//                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                        currIndex++;
                        if (currIndex >= hanzi_list.length){
                            currIndex=0;
                        }
                        currChar = hanzi_list[currIndex];

                        textDisplay.setText(currChar);
                    }

                    // Right to left swipe action
                    else
                    {
//                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                        currIndex--;
                        if (currIndex <= 0){
                            currIndex=hanzi_list.length-1;
                        }
                        currChar = hanzi_list[currIndex];

                        textDisplay.setText(currChar);
                    }

                }
                if (Math.abs(deltaY) > MIN_DISTANCE && Math.abs(deltaX) < MAX_DISTANCE)
                {
                    if (y2 > y1) {
                        score_list[currIndex]++;
                    }

                    else {
                        score_list[currIndex]--;
                    }
                }
                else
                {
                    // consider as something else - a screen tap for example
                }

                Toast.makeText(this, String.valueOf(score_list[currIndex]), Toast.LENGTH_SHORT).show ();
                break;
        }
        return super.onTouchEvent(event);
    }
}
