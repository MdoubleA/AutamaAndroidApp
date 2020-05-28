package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
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
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike
    private String userName; // Mike
    private String userPassword; // Mike
    float x1, x2, y1, y2;
    private ImageView myimage;
    private Button myMatch;
    private Button myDislike;
    private TextView ai_first, ai_last, ai_interest1, ai_interest2, ai_interest3, ai_interest4, ai_interest5, ai_interest6;
    private int currentPicture = -1;
    private Button Back;
    private JSONArray unmatchedAutama = null;
    private int currAutama = 0;
    private int temp [] = new int[100];
    int[] images = {R.drawable._ai1, R.drawable._ai2, R.drawable._ai3,R.drawable._ai4,R.drawable._ai5,R.drawable._ai6};
    private int counter = 0;
    private final String post_root = "http://10.0.2.2:8000";

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_matches);
        Intent intent = getIntent();
        userName = intent.getStringExtra(SecondActivity.USERNAME);
        userPassword = intent.getStringExtra(SecondActivity.USERPASSWORD);
        ai_first = (TextView)findViewById(R.id.tvname);
        ai_interest1 = (TextView)findViewById(R.id.int1);
        ai_interest2 = (TextView)findViewById(R.id.int2);
        ai_interest3 = (TextView)findViewById(R.id.int3);
        ai_interest4 = (TextView)findViewById(R.id.int4);
        ai_interest5 = (TextView)findViewById(R.id.int5);
        ai_interest6 = (TextView)findViewById(R.id.int6);
        myMatch = (Button)findViewById(R.id.btnMatch);
        myDislike = (Button)findViewById(R.id.btnDislike);
        myimage = (ImageView)findViewById(R.id.MyImage);
        Back = (Button)findViewById(R.id.btnBackTo2nd);

        final String post_url   = "http://10.0.2.2:8000/api/v1/unmatchedautama/";
        final String credential = Credentials.basic(userName, userPassword);
        final Request request   = new Request.Builder()
                .url(post_url)
                .header("Authorization", credential)
                .build();

        Thread serverCall = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();

                    JSONObject jResponse = new JSONObject(response.body().string());
                    unmatchedAutama = jResponse.getJSONArray("objects");
                    if (unmatchedAutama.length() == 0) {
                        Log.d("Error", "Looks you matched with all of them. ;)");
                        finish();
                        return;
                    }
                    else {
                        FindMatches.this.populateProfile();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        serverCall.start();
        try {
            serverCall.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { // back button
                FindMatches.this.previousScreen();
            }
        });

        myMatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currAutama < unmatchedAutama.length()) {
                    String autamaID = null;
                    try {
                        JSONObject anAutama = unmatchedAutama.getJSONObject(currAutama);
                        autamaID = anAutama.getString("id");
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
                }
                FindMatches.this.nextAutama();
            }
        });

        myDislike = (Button) findViewById(R.id.btnDislike);
        myDislike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FindMatches.this.nextAutama();
            }
        });
    }

    private void nextAutama() {
        currAutama++;
        FindMatches.this.populateProfile();
    }

    private void populateProfile() {
        if (currAutama < unmatchedAutama.length()) {
            //FindMatches.this.changepicture();
            String autamaID = null;
            String autamaPicture = null;
            String an_interest1 = null;
            String an_interest2 = null;
            String an_interest3 = null;
            String an_interest4 = null;
            String an_interest5 = null;
            String an_interest6 = null;
            String ai_first_name = null;
            String ai_last_name = null;
            JSONObject anAutama = null;
            try {
                anAutama = unmatchedAutama.getJSONObject(currAutama);
                if (anAutama != null) {
                    autamaID = anAutama.getString("id");
                    autamaPicture = anAutama.getString("picture");
                    an_interest1 = anAutama.getString("interest1");
                    an_interest2 = anAutama.getString("interest2");
                    an_interest3 = anAutama.getString("interest3");
                    an_interest4 = anAutama.getString("interest4");
                    an_interest5 = anAutama.getString("interest5");
                    an_interest6 = anAutama.getString("interest6");
                    ai_first_name = anAutama.getString("first");
                    ai_last_name = anAutama.getString("last");
                    ai_first.setText(ai_first_name + " " + ai_last_name);
                    ai_interest1.setText(an_interest1);
                    ai_interest2.setText(an_interest2);
                    ai_interest3.setText(an_interest3);
                    ai_interest4.setText(an_interest4);
                    ai_interest5.setText(an_interest5);
                    ai_interest6.setText(an_interest6);
                    FindMatches.this.displayPicture(autamaPicture);
                }
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            FindMatches.this.previousScreen();
        }
    }

    private void previousScreen() {
        Intent i = new Intent(FindMatches.this, SecondActivity.class);
        i.putExtra(MY_MATCHES, temp);
        i.putExtra(USERNAME, userName);
        i.putExtra(USERPASSWORD, userPassword);
        startActivity(i);
    }

    private void changepicture(){
        currentPicture++;
        currentPicture = currentPicture % images.length;
        myimage.setImageResource(images[currentPicture]);
    }

    private void displayPicture(String branch) throws InterruptedException {
        String post_url = post_root + branch;
        final Request request = new Request.Builder().url(post_url).build();

        Thread serverCall = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    myimage.setImageBitmap(bitmap);
                }
            }
        });
        serverCall.start();
        serverCall.join();
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
