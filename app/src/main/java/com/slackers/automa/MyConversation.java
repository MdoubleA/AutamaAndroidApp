package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyConversation extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    private Button back_btn;
    private ScrollView mscrollview;
    private TextView mydisplay;
    private Button sendbtn;
    private TextView message;
    private EditText send_message;
    float x1, x2, y1, y2;
    private int temp [] = new int[100];
    private int counter = 0;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myconversation);
        Intent intent = getIntent();
        mscrollview = (ScrollView)findViewById(R.id.scrollviewlayout);
        back_btn = (Button)findViewById(R.id.backbtn);
        message = (TextView)findViewById(R.id.mymessage);
        sendbtn = (Button)findViewById(R.id.sendbtn);
        send_message = (EditText)findViewById(R.id.entermessage);
        counter = intent.getIntExtra(Choose_Match.COUNTER, 0);
        temp = intent.getIntArrayExtra(Choose_Match.MY_MATCHES);
        count = intent.getIntExtra(Choose_Match.COUNT, 0);
        mydisplay = (TextView)findViewById(R.id.textView3);
        mydisplay.setText("Talking with AI: " + String.valueOf(counter));
        sendbtn.setText("Send Message");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyConversation.this, Choose_Match.class);
                i.putExtra(MY_MATCHES, temp);
                i.putExtra(COUNTER, counter);
                startActivity(i);
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add message and clear message
                Send_Message(send_message.getText().toString());
            }
        });
    }

    private void Send_Message(String message_to_send){
        message.append(message_to_send + "(User Message)");
        send_message.setText("");
        message.append("\n");
    //This is where the AI would send a reply
        message.append("(message from AI)");
        message.append("\n");
        mscrollview.fullScroll(ScrollView.FOCUS_DOWN);
        Close_Typer();
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
                /*if(x1 > x2){
                    Intent i = new Intent(SecondActivity.this, YourProfile.class);
                    startActivity(i);
                }*/
                if(x1 < x2){
                    Intent i = new Intent(MyConversation.this, Choose_Match.class);
                    i.putExtra(MY_MATCHES, temp);
                    i.putExtra(COUNTER, counter);
                    startActivity(i);
                }


                break;
        }
        return false;
    }
    private void Close_Typer(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
