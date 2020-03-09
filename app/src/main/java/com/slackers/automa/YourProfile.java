package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class YourProfile extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    private int temp [] = new int[100];
    private int counter = 0;
    float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Intent intent = getIntent();
        counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
    }
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                /*if(x1 < x2){
                    Intent i = new Intent(SecondActivity.this, YourProfile.class);
                    startActivity(i);
                }*/
                if(x1 > x2){
                    Intent i = new Intent(YourProfile.this, SecondActivity.class);
                    i.putExtra(MY_MATCHES, temp);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }


                break;
        }
        return false;
    }
}
