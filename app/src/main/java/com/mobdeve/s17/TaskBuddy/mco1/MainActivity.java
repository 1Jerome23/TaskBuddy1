package com.mobdeve.s17.TaskBuddy.mco1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ImageView RegisterLogo;
    EditText RegisterFullName;
    EditText RegisterEmail;
    EditText RegisterPassword;
    Button RegisterSubmit;
    TextView RegisterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegisterLogo = (ImageView) findViewById(R.id.RegisterLogo);
        RegisterFullName = (EditText) findViewById(R.id.RegisterFullName);
        RegisterEmail = (EditText) findViewById(R.id.RegisterEmail);
        RegisterPassword = (EditText) findViewById(R.id.RegisterPassword);
        RegisterSubmit = (Button) findViewById(R.id.RegisterSubmit);
        RegisterMessage = (TextView) findViewById(R.id.RegisterMessage);

        RegisterSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();

            }
        });
        SpannableString spannableString = new SpannableString(RegisterMessage.getText());

        ClickableSpan registerNowClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        };

        ClickableSpan logInClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent logInIntent = new Intent(MainActivity.this, login.class);
                startActivity(logInIntent);
            }
        };

        spannableString.setSpan(logInClickableSpan, RegisterMessage.getText().toString().indexOf("Log In"), RegisterMessage.getText().toString().indexOf("Log In") + "Log In".length(), 0);

        RegisterMessage.setText(spannableString);
        RegisterMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }
}



