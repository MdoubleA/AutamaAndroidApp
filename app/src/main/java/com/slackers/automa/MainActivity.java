package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Button NewAcount;
    private int counter = 5;
    private TextView MyFlag;
    public static final String USERNAME = "com.slackers.automa.USERNAME"; // Mike
    public static final String USERPASSWORD = "com.slackers.automa.USERPASSWORD"; // Mike

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyFlag = (TextView)findViewById(R.id.Flag);
        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvinfo);
        Login = (Button)findViewById(R.id.btnLogin);
        NewAcount = (Button)findViewById(R.id.btnnewac);


        // Link to create account
        NewAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(i);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(final String userName, final String userPassword){
        final String[] apikey = {null};
        String post_url   = "http://10.0.2.2:8000/api/v1/accounts/";
        String credential = Credentials.basic(userName, userPassword);
        Request request   = new Request.Builder()
                .url(post_url)
                .header("Authorization", credential)
                .header("Username", userName)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("HttpResponse", Integer.toString(response.code()));
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() != 200) {
                            counter--;
                            Info.setText("Wrong login information. No of attempts remaining: " + String.valueOf(counter));
                            if(counter == 0){
                                Login.setEnabled(false);
                            }
                        }
                        else {
                            Info.setText("Moving to the next screen.");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("objects");
                                JSONObject jUser = jsonArray.getJSONObject(0);
                                apikey[0] = jUser.getString("apikey");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Intent i = new Intent(MainActivity.this, SecondActivity.class);
                            i.putExtra(USERNAME, userName);
                            i.putExtra(USERPASSWORD, userPassword);
                            //i.putExtra(USERPASSWORD, apikey[0]);
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }
}
