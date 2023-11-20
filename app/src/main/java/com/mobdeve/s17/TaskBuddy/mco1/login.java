package com.mobdeve.s17.TaskBuddy.mco1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    ImageView LoginLogo;
    EditText LoginEmail;
    EditText LoginPassword;
    Button LoginSubmit;
    TextView LoginMessage;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseApp.initializeApp(this);

        LoginLogo = (ImageView)findViewById(R.id.LoginLogo);
        LoginEmail = (EditText)findViewById(R.id.LoginEmail);
        LoginPassword = (EditText) findViewById(R.id.LoginPassword);
        LoginSubmit = (Button) findViewById(R.id.LoginSubmit);
        LoginMessage = (TextView) findViewById(R.id.LoginMessage);

        //sharedPreferences
        sharedPreferences = getSharedPreferences("prefKey", MODE_PRIVATE);

        skipLogin();

        //function is called to skip the login page if a shared preference is present



        //SUBMIT INTENT
        LoginSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersRef = db.collection("UserData");

                    usersRef.whereEqualTo("email", email).whereEqualTo("password", password)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    String uid = queryDocumentSnapshots.getDocuments().get(0).getId();

                                    //save uid to sharedpref
                                    saveShared(uid);

                                    Intent intent = new Intent(login.this, homepage.class);
                                    intent.putExtra("uid", uid);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), "Log In Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Log In Failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirestoreError", "Error querying Firestore: " + e.getMessage());
                            });
                }
            }
            private void saveShared(String uid){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uid", uid);
                editor.commit();
            }
        });


        //LINK INTENT
        SpannableString spannableString = new SpannableString(LoginMessage.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, LoginMessage.getText().toString().indexOf("Register now"), LoginMessage.getText().toString().indexOf("Register now") + "Register now".length(), 0);
        LoginMessage.setText(spannableString);
        LoginMessage.setMovementMethod(LinkMovementMethod.getInstance());


    }
    private void skipLogin(){
        String saved = sharedPreferences.getString("uid",null);
        //skip to homepage if user is alr logged in
        if(saved != null){
            Intent intent = new Intent(login.this, homepage.class);
            intent.putExtra("uid", saved);
            startActivity(intent);
        }}

}