package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Choose_Match extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    public static final String COUNT = "com.slackers.automa.COUNT";
    public static final String AUTAMA_ID = "com.slackers.automa.AUTAMA_ID"; // Mike
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike
    private String userName;
    private String userPassword;
    float x1, x2, y1, y2;
    private Button newbtn;
    private int temp [] = new int[100];
    private int counter = 0;
    private int count = 0;
    private int autama_id = 4; // Mike this is Zara
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_match);
        Intent intent = getIntent();
        counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
        Button btn = (Button)findViewById(R.id.btn1);
        userName = intent.getStringExtra(SecondActivity.USERNAME); // Mike
        userPassword = intent.getStringExtra(SecondActivity.USERPASSWORD); // Mike
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
                    i.putExtra(AUTAMA_ID, autama_id); // Mike
                    i.putExtra(USERNAME, userName); // Mike
                    i.putExtra(USERPASSWORD, userPassword); // Mike

                    startActivity(i);
                }
            });
        }
    }
    public void add_buttons(){ // Mike
        int count;
        // Make server call with username and api_key
        // get ids of all matched autama int[] all_matches = [0,1,2...]
        // SET COUNTER TO NUMBER OF AUTAMA counter = all_matches.length()

        for (count = 1; count < counter + 1; count++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
            newbtn = new Button(this);
            //newbtn.setText("AI Name"+ String.valueOf(count)); //  String.toString(count)
            newbtn.setText(userName);
            if (userName == null) {
                Log.d("Failure:", "the password is null.");
            }
            else {
                Log.d("Success:", "The string is not null.");
            }

            layout.addView(newbtn);
        }

        autama_id = 4;
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
