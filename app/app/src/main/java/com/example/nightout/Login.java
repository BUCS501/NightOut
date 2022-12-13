package com.example.nightout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class Login extends AppCompatActivity {

    //Variables for onCreate
    EditText username, password;
    Button mbtn;
    DB dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.editTextTextPersonName3);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        mbtn = findViewById(R.id.button);
        dB = new DB(this);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Saves the user input
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String check = dB.getdatapass(user);
                //Checks to see if fields are blank
                if(user.equals("")||pass.equals(""))
                    Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                //Checks to see if there is an associated account
                else if (check.equals("")) {
                    Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Verifies that the encrypted passwords match using the special Bcrypt decoder
                    BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), check);
                    if(result.verified==true){
                        //Log in the existing user and launch the main activity
                        SignUp.user = user;
                        Toast.makeText(Login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Button for sending to the Signup activity
        Button btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }
    //Prevents circumnavigation of login proccess
    @Override
    public void onBackPressed() {

    }
}