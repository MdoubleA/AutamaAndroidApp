package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyConversation extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike
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
    private int autama_id = 0;
    private String userName;
    private String userPassword;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build(); // Mike

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

        autama_id = intent.getIntExtra(Choose_Match.AUTAMA_ID, 0); // Mike
        userName = intent.getStringExtra(Choose_Match.USERNAME); // Mike
        userPassword = intent.getStringExtra(Choose_Match.USERPASSWORD); // Mike

        mydisplay = (TextView)findViewById(R.id.textView3);
        mydisplay.setText("Talking with AI: " + String.valueOf(counter));
        sendbtn.setText("Send Message");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyConversation.this, Choose_Match.class);
                i.putExtra(MY_MATCHES, temp);
                i.putExtra(COUNTER, counter);
                i.putExtra(USERNAME, userName);
                i.putExtra(USERPASSWORD, userPassword);
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
        String post_url = "http://10.0.2.2:8000/api/v1/messages/?username=" + userName + "&" + "api_key=" + userPassword;
        message.append(message_to_send + "(User Message)");
        send_message.setText("");
        message.append("\n");


        JSONObject post_data = new JSONObject();
        try {
            post_data.put("message", message_to_send);
            post_data.put("sender", "User");
            post_data.put("autamaID", autama_id);
            post_data.put("userID", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody post = RequestBody.create(
                MediaType.parse("application/json"), post_data.toString());

        //String credential = Credentials.basic("jordan", "a");

        Request request = new Request.Builder()
                .url(post_url)
                //.header("Authorization", credential)
                .header("AutamaID", Integer.toString(autama_id))
                .post(post)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                MyConversation.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //This is where the AI would send a reply
                        if (response.code() == 201) {
                            message.append("(message from AI) " + Integer.toString(autama_id) + " "); // Mike
                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String autama_response = jsonObject.getString("message");

                                message.append(autama_response); // Mike
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            message.append("\n");
                            mscrollview.fullScroll(ScrollView.FOCUS_DOWN);
                            Close_Typer();
                        }
                        else {
                            message.append("There has been an error");
                            try {
                                message.append(response.body().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            message.append("\n");
                            mscrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }
                });
            }
        });
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
