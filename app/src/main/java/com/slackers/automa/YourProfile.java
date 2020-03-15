package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class YourProfile extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    private int temp [] = new int[100];
    private int counter = 0;
    float x1, x2, y1, y2;
    private TextView display_name;
    private TextView display_lastname;
    private TextView interest_1;
    private TextView interest_2;
    private TextView interest_3;
    private TextView interest_4;
    private TextView interest_5;
    private TextView my_bio;
    private TextView my_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Intent intent = getIntent();
        counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
        display_name = (TextView)findViewById(R.id.tvname);
        display_lastname = (TextView)findViewById(R.id.tvlastname);
        interest_1 = (TextView)findViewById(R.id.int1);
        interest_2 = (TextView)findViewById(R.id.int2);
        interest_3 = (TextView)findViewById(R.id.int3);
        interest_4 = (TextView)findViewById(R.id.int4);
        interest_5 = (TextView)findViewById(R.id.int5);
        my_bio = (TextView)findViewById(R.id.bio);
        my_email = (TextView)findViewById(R.id.email);
        Disiplay_Profile();
    }

    private void Disiplay_Profile() {
        display_name.setText("Admin");//grab dynamicly later
        display_lastname.setText("Herrera");
        interest_1.setText("temp interest1");
        interest_2.setText("temp interest2");
        interest_3.setText("temp interest3");
        interest_4.setText("temp interest4");
        interest_5.setText("temp interest5");
        my_email.setText("temp email");
        my_bio.setText("this is where all of my information could be, \n \n scroll down and more information \n\n\n Testing Scrolling");
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
