package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Button NewAcount;
    private int counter = 5;
    private TextView MyFlag;
    //private RequestQueue mQueue;
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

        //if(userName.equals("MyEmail") && userPassword.equals("Password")){
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        //}else{
          //  counter--;
          //  Info.setText("No of attempts remaining: " + String.valueOf(counter));
           // if(counter == 0){
            //    Login.setEnabled(false);
          //  }
        //}
    }
}
