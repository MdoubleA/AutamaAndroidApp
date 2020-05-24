package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindMatches extends AppCompatActivity {
    public static final String MY_MATCHES = "com.slackers.automa.MY_MATCHES";
    //public static final String COUNTER = "com.slackers.automa.COUNTER";
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
    private JSONArray unmatchedAutama = null;
    private int currAutama = 0;
    private int temp [] = new int[100];
    int[] images = {R.drawable._ai1, R.drawable._ai2, R.drawable._ai3,R.drawable._ai4,R.drawable._ai5,R.drawable._ai6};
    private int counter = 0;

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_matches);
        myMatch = (Button)findViewById(R.id.btnMatch);
        myDislike = (Button)findViewById(R.id.btnDislike);
        Intent intent = getIntent();
        //counter = intent.getIntExtra(SecondActivity.COUNTER, 0);
        //temp = intent.getIntArrayExtra(SecondActivity.MY_MATCHES);
        myimage = (ImageView)findViewById(R.id.MyImage);
        Back = (Button)findViewById(R.id.btnBackTo2nd);
        userName = intent.getStringExtra(SecondActivity.USERNAME);
        userPassword = intent.getStringExtra(SecondActivity.USERPASSWORD);

        final String post_url   = "http://10.0.2.2:8000/api/v1/unmatchedautama/";
        final String credential = Credentials.basic(userName, userPassword);
        final Request request   = new Request.Builder()
                .url(post_url)
                .header("Authorization", credential)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();

                    JSONObject jResponse = new JSONObject(response.body().string());
                    unmatchedAutama      = jResponse.getJSONArray("objects");
                    if (unmatchedAutama.length() == 0) {
                        Log.d("Error", "Looks you matched with all of them. ;)");
                        finish();
                        return;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { // back button
                FindMatches.this.previousScreen();
            }
        });

        myMatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currAutama < unmatchedAutama.length()) {
                    String autamaID = null;
                    String an_interest = null;
                    try {
                        JSONObject anAutama = unmatchedAutama.getJSONObject(currAutama);
                        autamaID = anAutama.getString("id");
                        an_interest = anAutama.getString("interest6");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject post_data = new JSONObject();
                    try {
                        post_data.put("userID", userName);
                        post_data.put("autamaID", autamaID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody post = RequestBody.create(
                            MediaType.parse("application/json"), post_data.toString());

                    final Request matchRequest   = new Request.Builder()
                            .url(post_url)
                            .header("Authorization", credential)
                            .post(post)
                            .build();

                    client.newCall(matchRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("Response Code", Integer.toString(response.code()));
                            Log.d("Response Body", response.body().string());
                        }
                    });

                    changepicture();
                    currAutama++;
                }
                else {
                    FindMatches.this.previousScreen();
                }
            }
        });


        myDislike = (Button) findViewById(R.id.btnDislike);
        myDislike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currAutama< unmatchedAutama.length()) {
                    currAutama++;
                    changepicture();
                }
                else {
                    FindMatches.this.previousScreen();
                }
            }
        });
    }

    private void previousScreen() {
        Intent i = new Intent(FindMatches.this, SecondActivity.class);
        i.putExtra(MY_MATCHES, temp);
        //i.putExtra(COUNTER, counter);
        i.putExtra(USERNAME, userName);
        i.putExtra(USERPASSWORD, userPassword);
        startActivity(i);
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
