package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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


    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build();


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
        String url = "http://10.0.2.2:8000/api/v1/autamas/?format=json";
        Request request = new Request.Builder()
                .url(url)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse  = response.body().string();
                    try {
                        JSONObject Jobject  = new JSONObject(myResponse);
                        JSONArray Jarray    = Jobject.getJSONArray("objects");
                        JSONObject anAutama = Jarray.getJSONObject(0);

                        final String first = anAutama.getString("first");//grab dynamically later
                        final String last = anAutama.getString("last");
                        final String interest1 = anAutama.getString("interest1");
                        final String interest2 = anAutama.getString("interest2");
                        final String interest3 = anAutama.getString("interest3");
                        final String interest4 = anAutama.getString("interest4");
                        final String interest5 = anAutama.getString("interest5");
                        //final Strin

                        YourProfile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                display_name.setText(first);//grab dynamically later
                                display_lastname.setText(last);
                                interest_1.setText(interest1);
                                interest_2.setText(interest2);
                                interest_3.setText(interest3);
                                interest_4.setText(interest4);
                                interest_5.setText(interest5);
                                my_email.setText("Yahoo@420swag");
                                my_bio.setText("this is where all of my information could be, \n \n scroll down and more information \n\n\n Testing Scrolling");
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
