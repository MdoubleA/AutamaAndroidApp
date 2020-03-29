package com.slackers.automa;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//scrollView.getLayoutParams().height = yourNewHeight; // in pixels
// scrollView.getLayoutParams().width = yourNewWidth; // in pixels
import java.util.Arrays;
import java.util.Vector;
/*
user_id all variables
User_name() functions
UserInformation classes
 */

// Second Activity is the HOME page right now, which connects all the other pages
public class SecondActivity extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    private Button switchToAi;
    private Button findMatchesbtn;
    private int[] myMatches = new int[100];

    //private Vector<Integer> myMatches = new Vector<>();
    private TextView Number_of_matches;
    private int counter = 0;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        Intent intent = getIntent();
        myMatches = intent.getIntArrayExtra(FindMatches.MY_MATCHES);
        counter = intent.getIntExtra(FindMatches.COUNTER, 0);
        myMatches = intent.getIntArrayExtra(YourProfile.MY_MATCHES);
        counter = intent.getIntExtra(YourProfile.COUNTER, 0);
        Number_of_matches= (TextView)findViewById(R.id.tvNumberOfMatches);
        Number_of_matches.setText("No of Matches: " + String.valueOf(counter));

        switchToAi = (Button)findViewById(R.id.buttonai);
        switchToAi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(SecondActivity.this, Choose_Match.class);
                i.putExtra(MY_MATCHES, myMatches);
                i.putExtra(COUNTER, counter);
                startActivity(i);
            }
        });
        findMatchesbtn = (Button)findViewById(R.id.btnFindMatches);
        findMatchesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, FindMatches.class);
                i.putExtra(MY_MATCHES, myMatches);
                i.putExtra(COUNTER, counter);
                startActivity(i);
            }
        });
       // count();
    }
/*
    public int count(){
        counter = myMatches.size();
        Number_of_matches.setText("" + String.valueOf(counter));
        return 1;
    }*/
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if(x1 < x2){
                    Intent i = new Intent(SecondActivity.this, YourProfile.class);
                    i.putExtra(MY_MATCHES, myMatches);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }
                if(x1 > x2){
                    Intent i = new Intent(SecondActivity.this, Choose_Match.class);
                    i.putExtra(MY_MATCHES, myMatches);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }


                break;
        }
        return false;
    }
}
