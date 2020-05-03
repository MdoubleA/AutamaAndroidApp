package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Button NewAcount;
    private int counter = 5;
    private TextView MyFlag;
    private RequestQueue mQueue;
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
    private void validate(String userName, String userPassword){

        mQueue = Volley.newRequestQueue(this);
        String url ="http://10.0.2.2:8000/api/v1/autamaprofiles/?=json&username=Michael&api_key=1a23";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("objects");

                            for (int e = 0; e <= 0; e++) {
                                JSONObject an_autama = jsonArray.getJSONObject(e);
                                String first = an_autama.getString("first");

                                // Print
                                Info.setText(first);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mQueue.add(request);

        if(userName.equals("MyEmail") && userPassword.equals("Password")){
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        }else{
            counter--;
            //Info.setText("No of attempts remaining: " + String.valueOf(counter));
            if(counter == 0){
                //Login.setEnabled(false);
            }
        }
    }
}
