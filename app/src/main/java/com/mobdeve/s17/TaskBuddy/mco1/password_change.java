package com.mobdeve.s17.TaskBuddy.mco1;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class password_change extends AppCompatActivity {


    ImageView Password_icon;
    TextView PasswordHeader;
    TextView PasswordOld;
    EditText PasswordInputCurrent;
    TextView PasswordNew;
    EditText PasswordInputNew;
    TextView PasswordEnter;
    EditText PasswordInputEnter;
    Button PasswordButton;
    ImageView PasswordFooter;
    ImageButton Password_homepage;
    ImageButton Password_profile;
    Button Password_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);

        ImageView Password_icon = findViewById(R.id.Password_icon);
        TextView PasswordHeader = findViewById(R.id.PasswordHeader);
        TextView PasswordOld = findViewById(R.id.PasswordOld);
        EditText PasswordInputCurrent = findViewById(R.id.PasswordInputCurrent);
        TextView PasswordNew = findViewById(R.id.PasswordNew);
        EditText PasswordInputNew = findViewById(R.id.PasswordInputNew);
        TextView PasswordEnter = findViewById(R.id.PasswordEnter);
        EditText PasswordInputEnter = findViewById(R.id.PasswordInputEnter);
        Button PasswordButton = findViewById(R.id.PasswordButton);
        ImageView PasswordFooter = findViewById(R.id.PasswordFooter);
        ImageButton Password_homepage = findViewById(R.id.Password_homepage);
        ImageButton Password_profile = findViewById(R.id.Password_profile);
        Button Password_back = findViewById(R.id.Password_back);

        Password_back.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,profile.class);
                startActivity(intent);
            }
        });
        //POPUP MESSAGE
        PasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully updated the password", Toast.LENGTH_SHORT).show();
            }
        });
        //FOOTER INTENT
        Password_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,homepage.class);
                startActivity(intent);
            }
        });

        Password_profile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,profile.class);
                startActivity(intent);
            }
        });

    }}

