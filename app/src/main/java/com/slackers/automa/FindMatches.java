package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Vector;

public class FindMatches extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike
    private String userName; // Mike
    private String userPassword; // Mike
    float x1, x2, y1, y2;
    private ImageView myimage;
    private Button myMatch;
    private Button myDislike;
    private int currentPicture;
    private Button Back;
    private int temp [] = new int[100];
    int[] images = {R.drawable._ai1, R.drawable._ai2, R.drawable._ai3,R.drawable._ai4,R.drawable._ai5,R.drawable._ai6};
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_matches);
        myMatch = (Button)findViewById(R.id.btnMatch);
        myDislike = (Button)findViewById(R.id.btnDislike);
        Intent intent = getIntent();
        counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
        myimage = (ImageView)findViewById(R.id.MyImage);
        Back = (Button)findViewById(R.id.btnBackTo2nd);
        userName = intent.getStringExtra(SecondActivity.USERNAME);
        userPassword = intent.getStringExtra(SecondActivity.USERPASSWORD);
        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { // back button
                Intent i = new Intent(FindMatches.this, SecondActivity.class);
                i.putExtra(MY_MATCHES, temp);
                i.putExtra(COUNTER, counter);
                i.putExtra(USERNAME, userName);
                i.putExtra(USERPASSWORD, userPassword);
                startActivity(i);
            }
        });

        myMatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                counter++;
                changepicture();
            }
        });

        myDislike = (Button)findViewById(R.id.btnDislike);
        myDislike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                changepicture();
            }
        });
    }

    private void changepicture(){
        currentPicture++;
        currentPicture = currentPicture % images.length;
        myimage.setImageResource(images[currentPicture]);
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
                if(x1 < x2){
                    changepicture();
                }
                if(x1 > x2){
                    counter++;
                    changepicture();
                }


                break;
        }
        return false;
    }
}
