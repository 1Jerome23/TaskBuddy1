package com.mobdeve.s17.TaskBuddy.mco1;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("uid")) {
            String uid = intent.getStringExtra("uid");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("UserData").document(uid);

            Password_back.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,profile.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        //POPUP MESSAGE
            PasswordButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Get the current password, new password, and confirm new password from user input
                    String currentPassword = PasswordInputCurrent.getText().toString().trim();
                    String newPassword = PasswordInputNew.getText().toString().trim();
                    String confirmPassword = PasswordInputEnter.getText().toString().trim();

                    // Validate the input fields
                    if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if the new password and confirm password match
                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(getApplicationContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("UserData").document(uid);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String storedPassword = documentSnapshot.getString("password");

                                if (currentPassword.equals(storedPassword)) {
                                    userRef.update("password", newPassword)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "You have successfully updated the password", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(password_change.this, homepage.class);
                                                    intent.putExtra("uid", uid);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            //FOOTER INTENT
        Password_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,homepage.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        Password_profile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (password_change.this,profile.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

    }}}

