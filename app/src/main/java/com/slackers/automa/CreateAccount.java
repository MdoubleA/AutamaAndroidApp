package com.slackers.automa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateAccount extends AppCompatActivity {
    private EditText NewUsername;
    private EditText NewPassword;
    private Button CreateNAccount;
    private Button Back;
    private TextView Info;


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
        Info.setText("Searching...");
    }
}
