package com.example.nightout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUp extends AppCompatActivity {

    boolean checksymbol = false;
    boolean checkcap = false;
    String[] digits = {"1","2","3","4","5","6","7","8","9","0","!","@","#","$","%","^","&","*"};
    String[] cap = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    EditText username, password,repassword,name;
    static String namer;
    public static String user;
    public static String placer;
    Button mbtn;
    DB dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.editTextTextPersonName);
        username = (EditText) findViewById(R.id.editTextTextPersonName2);
        password = (EditText) findViewById(R.id.editTextTextPersonName3);
        repassword = (EditText) findViewById(R.id.editTextTextPassword);
        mbtn = findViewById(R.id.button);
        dB = new DB(this);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namer = name.getText().toString();
                user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String bcryptHashString = BCrypt.withDefaults().hashToString(12, (pass.toCharArray()));
                if(user.equals("")||pass.equals("")||repass.equals("")||namer.equals(""))
                    Toast.makeText(SignUp.this, "Please enter all the required fields", Toast.LENGTH_SHORT).show();
                else{
                    if (isEmailValid(user)){

                    if(pass.equals(repass)) {
                        for (int i = 0; i < digits.length; i++) {
                            if (pass.contains(digits[i])) {
                                checksymbol = true;
                            }
                        }
                        for (int i = 0; i < cap.length; i++) {
                            if (pass.contains(cap[i])) {
                                checkcap = true;
                            }
                        }
                        if ((pass.length() > 8) && (checksymbol == true) &&(checkcap == true)) {
                            Boolean checkuser = dB.checkusername(user);
                            if (checkuser == false) {
                                Boolean insert = dB.insertData(user, bcryptHashString,namer);
                                if (insert == true) {
                                    Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Must be at least 9 characters, must contain a capital letter, must contain a number or special character", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(SignUp.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }}else{
                            Toast.makeText(SignUp.this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                        }
                }}
        });

        Button btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}