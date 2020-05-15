package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAccount extends AppCompatActivity {
    private EditText NewUsername;
    private EditText NewPassword;
    private Button CreateNAccount;
    private Button Back;
    private TextView Info;

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.MODERN_TLS))
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        NewUsername = (EditText)findViewById(R.id.usernamenew);
        NewPassword = (EditText)findViewById(R.id.passwordnew);
        CreateNAccount = (Button) findViewById(R.id.createaccount);
        Back = (Button) findViewById(R.id.backfromcreateaccount);
        Info = (TextView) findViewById(R.id.myerror);


        // Here is were would track login. Memory wiped after app close.
        CreateNAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdatabase(NewUsername.getText().toString(), NewPassword.getText().toString());
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkdatabase(String Username, String Password){
        String post_url = "http://10.0.2.2:8000/api/v1/accounts/";
        //String post_data = "{\\n\"username\":\"jordan\",\\n\"password\":\"a\"\\n}";

        JSONObject post_data = new JSONObject();
        try {
            post_data.put("username", "jordan");
            post_data.put("password", "a");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody postHeaderAndBody = RequestBody.create(
                MediaType.parse("application/json"), post_data.toString());


        Request request = new Request.Builder()
                .url(post_url)
                .post(postHeaderAndBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                CreateAccount.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Info.setText(response.code());
                    }
                });

            }
        });



        //Info.setText("Searching...");
    }
}
