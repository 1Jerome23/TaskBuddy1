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

import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {
    ImageView profile_header;
    ImageView profile_icon;
    TextView profile_text;
    Button profile_edit;
    Button profile_logout;
    Button profile_change_pw;
    TextView profile_change_name;
    TextView profile_name;
    ImageView profile_footer;
    ImageButton profile_homepage;
    ImageButton profile_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        profile_header = (ImageView) findViewById(R.id.profile_header);
        profile_icon = (ImageView) findViewById(R.id.profile_icon);
        profile_text = (TextView)  findViewById(R.id.profile_text);
        profile_edit =  (Button)findViewById(R.id.profile_edit);
        profile_logout = (Button) findViewById(R.id.profile_logout);
        profile_change_pw = (Button) findViewById(R.id.profile_change_pw);
        profile_change_name = (TextView) findViewById(R.id.profile_change_name);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_footer = (ImageView) findViewById(R.id.profile_footer);
        profile_homepage = (ImageButton) findViewById(R.id.profile_homepage);
        profile_profile = (ImageButton) findViewById(R.id.profile_profile);


        //FOOTER INTENT
        profile_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (profile.this,homepage.class);
                startActivity(intent);
            }
        });


        //BUTTON INTENT
        profile_edit.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (profile.this,edit_profile.class);
                startActivity(intent);
            }
        });
        profile_change_pw.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (profile.this,password_change.class);
                startActivity(intent);
            }
        });
        profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
    }
