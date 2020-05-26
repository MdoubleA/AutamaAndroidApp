package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Choose_Match extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    public static final String COUNTER = "com.slackers.automa.COUNTER";
    public static final String COUNT = "com.slackers.automa.COUNT";
    public static final String AUTAMA_ID = "com.slackers.automa.AUTAMA_ID"; // Mike
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike
    public static final String AUTAMAFIRST = "com.slackers.automa.AUTAMAFIRST";
    public static final String AUTAMALAST = "com.slackers.automa.AUTAMALAST";
    private String userName;
    private String userPassword;
    float x1, x2, y1, y2;
    private Button newbtn;
    private int temp [] = new int[100];
    private int counter = 0;
    private int count = 0;
    private int autama_id; // Mike this is Zara

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build(); // Mike

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
        if (userName == null) {
            userName = intent.getStringExtra(MyConversation.USERNAME);
            userPassword = intent.getStringExtra(MyConversation.USERPASSWORD);
        }

        String post_url   = "http://10.0.2.2:8000/api/v1/mymatches/";
        String credential = Credentials.basic(userName, userPassword);
        Request request   = new Request.Builder()
                .url(post_url)
                .header("Authorization", credential)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Choose_Match.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jResponse = new JSONObject(response.body().string());
                            JSONArray  matches   = jResponse.getJSONArray("objects");
                            Choose_Match.this.counter = matches.length();

                            if (counter > 0) {
                                Button[] btn = new Button[counter];
                                LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
                                for(int i = 0; i < counter; i++) {
                                    JSONObject a_match = matches.optJSONObject(i);
                                    autama_id = a_match.getInt("autamaID");
                                    final String autamaFirstName = a_match.getString("autamaFirstName");
                                    final String autamaLastName = a_match.getString("autamaLastName");

                                    btn[i] = new Button(Choose_Match.this);
                                    btn[i].setId(i);
                                    btn[i].setText("Autama: " + autamaFirstName + " " + autamaLastName + "(" + Integer.toString(autama_id) + ")");
                                    layout.addView(btn[i]);
                                    btn[i].setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            //your desired functionality
                                            Intent i = new Intent(Choose_Match.this, MyConversation.class);
                                            i.putExtra(MY_MATCHES, temp);
                                            i.putExtra(COUNTER, counter);
                                            i.putExtra(COUNT, count);
                                            i.putExtra(AUTAMA_ID, autama_id); // Mike
                                            i.putExtra(USERNAME, userName); // Mike
                                            i.putExtra(USERPASSWORD, userPassword); // Mike
                                            i.putExtra(AUTAMAFIRST, autamaFirstName);
                                            i.putExtra(AUTAMALAST, autamaLastName);
                                            startActivity(i);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Choose_Match.this, SecondActivity.class);
                i.putExtra(MY_MATCHES, temp);
                i.putExtra(COUNTER, counter);
                i.putExtra(USERNAME, userName); // Mike
                i.putExtra(USERPASSWORD, userPassword); // Mike
                startActivity(i);
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