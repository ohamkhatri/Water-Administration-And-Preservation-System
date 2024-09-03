package com.example.resueforce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView username=(TextView) findViewById(R.id.username);
        TextView password=(TextView) findViewById(R.id.password);

        MaterialButton loginbtn=(MaterialButton) findViewById(R.id.loginbtn);




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("kavyanshi2222@gmail.com") && password.getText().toString().equals("helloo")){
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,AdministratorActivity.class));
                }
                else if (username.getText().toString().equals("rescueforce04@gmail.com") && password.getText().toString().equals("helloo1")){
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,UserActivity.class));
                }
                else if (username.getText().toString().endsWith("ishikachowdhary2001@gmail.com") && password.getText().toString().equals("helloo2")){
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,UserActivity2.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}