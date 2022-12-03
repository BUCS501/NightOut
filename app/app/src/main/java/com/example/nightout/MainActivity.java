package com.example.nightout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button mbtn;
    DB dB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.editTextTextPersonName3);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        mbtn = findViewById(R.id.button);
        dB = new DB(this);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String check = dB.getdatapass(user);

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), check);
                   // Boolean checkuserpass = dB.checkusernamepassword(user, bcryptHashString);
                    if(result.verified==true){
                        SignUp.user = user;
                        Toast.makeText(MainActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), CentralActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Button btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
    }
}