package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Choose_Match extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    public static final String COUNT = "com.slackers.automa.COUNT";
    float x1, x2, y1, y2;
    private Button newbtn;
    private int temp [] = new int[100];
    private int counter = 0;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_match);
        Intent intent = getIntent();
        counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
        Button btn = (Button)findViewById(R.id.btn1);
        add_buttons();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Choose_Match.this, SecondActivity.class);
                i.putExtra(MY_MATCHES, temp);
                i.putExtra(COUNTER, counter);
                startActivity(i);
            }
        });
        if(counter > 0) {
            newbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Choose_Match.this, MyConversation.class);
                    i.putExtra(MY_MATCHES, temp);
                    i.putExtra(COUNTER, counter);
                    i.putExtra(COUNT, count);
                    startActivity(i);
                }
            });
        }
    }
    public void add_buttons(){
        int count;
        for (count = 1; count < counter + 1; count++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
            newbtn = new Button(this);
            newbtn.setText("AI Name"+ String.valueOf(count));
            layout.addView(newbtn);
        }
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
                    Intent i = new Intent(Choose_Match.this, SecondActivity.class);
                    i.putExtra(MY_MATCHES, temp);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }
                if(x1 > x2){
                    Intent i = new Intent(Choose_Match.this, SecondActivity.class);
                    i.putExtra(MY_MATCHES, temp);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }


                break;
        }
        return false;
    }
}
