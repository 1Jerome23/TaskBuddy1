package com.mobdeve.s17.TaskBuddy.mco1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {

    ImageView RegisterLogo;
    EditText RegisterFullName;
    EditText RegisterEmail;
    EditText RegisterPassword;
    Button RegisterSubmit;
    TextView RegisterMessage;
    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;


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

        //sharedPreferences
        sharedPreferences = getSharedPreferences("prefKey", MODE_PRIVATE);
        //function is called to skip the login page if a shared preference is present
        skipLogin();

        db = FirebaseFirestore.getInstance();


        RegisterSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fullName = RegisterFullName.getText().toString().trim();
                String email = RegisterEmail.getText().toString().trim();
                String plainTextpassword = RegisterPassword.getText().toString(); //plaintext

                if (TextUtils.isEmpty(fullName)) {
                    RegisterFullName.setError("Please enter Full Name");
                } else if (!isValidEmail(email)) { // This line checks the email's validity
                    RegisterEmail.setError("Please enter a valid Email Address");
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(plainTextpassword)) {
                    RegisterPassword.setError("Please enter Password");
                } else {
                    String uid = generateUniqueUid(email);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersRef = db.collection("UserData");

                    //hash plainTextpassword and store it into password variable
                    String password = BCrypt.withDefaults().hashToString(12, plainTextpassword.toCharArray());


                    usersRef.whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    addDataToFirestore(fullName, email, password, uid);
                                    Intent intent = new Intent(MainActivity.this, homepage.class);
                                    intent.putExtra("uid", uid);
                                    startActivity(intent);

                                } else {
                                    RegisterEmail.setError("Email already in use");
                                    Toast.makeText(getApplicationContext(), "Email is already in use", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirestoreError", "Error querying Firestore: " + e.getMessage());
                            });

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
    private String generateUniqueUid(String email) {
        String timestamp = String.valueOf(System.currentTimeMillis());

        String randomString = generateRandomString();

        return timestamp + "_" + randomString;
    }

    private String generateRandomString() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 10;

        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            randomString.append(allowedChars.charAt(index));
        }

        return randomString.toString();
    }


    private void addDataToFirestore(String fullName, String email, String password, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("UserData");

        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("password", password);
        user.put("uid", uid);

        usersRef.document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the case where adding user data to Firestore failed
                        Log.e("FirestoreError", "Error adding user data: " + e.getMessage());
                    }
                });

    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void skipLogin() {
        String saved = sharedPreferences.getString("uid", null);
        //skip to homepage if user is alr logged in
        if (saved != null) {
            Intent intent = new Intent(MainActivity.this, homepage.class);
            intent.putExtra("uid", saved);
            startActivity(intent);
        }
    }
}





