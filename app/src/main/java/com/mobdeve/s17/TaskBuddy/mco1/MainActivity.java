package com.mobdeve.s17.TaskBuddy.mco1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    ImageView RegisterLogo;
    EditText RegisterFullName;
    EditText RegisterEmail;
    EditText RegisterPassword;
    Button RegisterSubmit;
    TextView RegisterMessage;
    private String fullName;
    private String email;
    private String password;
    private FirebaseFirestore db;


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

        db = FirebaseFirestore.getInstance();


        RegisterSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);

                String fullName = RegisterFullName.getText().toString().trim();
                String email = RegisterEmail.getText().toString().trim();
                String password = RegisterPassword.getText().toString();

                if (TextUtils.isEmpty(fullName)) {
                    RegisterFullName.setError("Please enter Full Name");
                } else if (TextUtils.isEmpty(email)) {
                    RegisterEmail.setError("Please enter Email");
                } else if (TextUtils.isEmpty(password)) {
                    RegisterPassword.setError("Please enter Password");
                } else {
                    addDataToFirestore(fullName, email, password);
                }
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

    private void addDataToFirestore(String fullName, String email, String password) {

        CollectionReference dbData = db.collection("UserData");
        UserData data = new UserData(fullName,email,password);
        dbData.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(MainActivity.this, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



