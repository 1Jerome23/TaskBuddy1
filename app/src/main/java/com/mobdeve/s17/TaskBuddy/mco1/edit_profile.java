package com.mobdeve.s17.TaskBuddy.mco1;


import android.content.Intent;
import android.media.Image;
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

import org.w3c.dom.Text;

public class edit_profile extends AppCompatActivity {

    ImageView edit_header;
    ImageView edit_icon;
    TextView editTag;
    TextView edit_old;
    TextView edit_name;
    EditText edit_password;
    Button edit_confirm;
    ImageButton edit_homepage;
    ImageButton edit_calendar;
    ImageButton edit_profile;
    ImageView edit_footer;
    Button edit_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        ImageView edit_header = findViewById(R.id.edit_header);
        ImageView edit_icon = findViewById(R.id.edit_icon);
        TextView editTag = findViewById(R.id.editTag);
        TextView edit_old = findViewById(R.id.edit_old);
        TextView edit_name = findViewById(R.id.edit_name);
        EditText edit_password = findViewById(R.id.edit_password);
        Button edit_confirm = findViewById(R.id.edit_confirm);
        ImageButton edit_homepage = findViewById(R.id.edit_homepage);
        ImageButton edit_profile = findViewById(R.id.edit_profile);
        ImageView edit_footer = findViewById(R.id.edit_footer);
        Button edit_back = findViewById(R.id.edit_back);

        edit_back.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (edit_profile.this,profile.class);
                startActivity(intent);
            }
        });

        edit_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (edit_profile.this,homepage.class);
                startActivity(intent);
            }
        });


        edit_confirm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "You have successfully updated your name", Toast.LENGTH_SHORT).show();
            }
        });
        //FOOTER INTENT
        edit_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (edit_profile.this,homepage.class);
                startActivity(intent);
            }
        });


        edit_profile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (edit_profile.this,profile.class);
                startActivity(intent);
            }
        });
    }
}
