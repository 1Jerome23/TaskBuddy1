package com.mobdeve.s17.TaskBuddy.mco1;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import okio.Source;

public class taskDetailsActivity extends AppCompatActivity {

    TextView task_name;
    TextView task_description;
    TextView task_date;
    TextView task_status;
    TextView task_priority;
    private String uid = "";

    //reload counter for homepage
    int reloadCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);

        Intent intent = getIntent();
        if (intent != null) {
            String taskName = intent.getStringExtra("task_name");
            String taskDate = intent.getStringExtra("task_date");
            String taskStatus = intent.getStringExtra("task_status");
            String taskPriority = intent.getStringExtra("task_priority");
            String description = intent.getStringExtra("description");
            String imageURL = intent.getStringExtra("imageUrl");
            String taskId = getIntent().getStringExtra("taskId");
            String uid = getIntent().getStringExtra("uid");

            task_name = findViewById(R.id.task_name);
            task_date = findViewById(R.id.task_date);
            task_status = findViewById(R.id.task_status);
            task_priority = findViewById(R.id.task_priority);
            task_description = findViewById(R.id.task_description);

            task_name.setText(taskName);
            task_date.setText(taskDate);
            task_status.setText(taskStatus);
            task_priority.setText(taskPriority);
            task_description.setText(description);

            ImageView imageView = findViewById(R.id.add_file);
            if (imageURL != null && !imageURL.isEmpty()) {
                Picasso.get().load(imageURL).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PicassoDebug", "Image loaded successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PicassoDebug", "Error loading image: " + e.getMessage(), e);
                    }
                });
            } else {
                Log.d("PicassoDebug", "ImageURL is null or empty. Setting visibility to GONE.");

                imageView.setVisibility(View.GONE);
            }

        }

        Button Back_button = findViewById(R.id.Back_button);
        Button Edit_button = findViewById(R.id.Edit_button);

        Back_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(taskDetailsActivity.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });
        Edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from the TextViews and other UI elements
                String taskName = task_name.getText().toString();
                String taskDate = task_date.getText().toString();
                String taskStatus = task_status.getText().toString();
                String taskPriority = task_priority.getText().toString();
                String description = task_description.getText().toString();
                String imageURL = getIntent().getStringExtra("imageUrl");
                String taskId = getIntent().getStringExtra("taskId");
                String uid = getIntent().getStringExtra("uid");

                Intent editIntent = new Intent(taskDetailsActivity.this, edit_task.class);

                editIntent.putExtra("taskName", taskName);
                editIntent.putExtra("taskDate", taskDate);
                editIntent.putExtra("taskStatus", taskStatus);
                editIntent.putExtra("taskPriority", taskPriority);
                editIntent.putExtra("description", description);
                editIntent.putExtra("imageUrl", imageURL);
                editIntent.putExtra("taskId", taskId);
                editIntent.putExtra("uid", uid);
                Log.d("task_details", "uid: " + uid);
                Log.d("task_details", "taskName: " + taskName);
                Log.d("task_details", "description: " + description);
                Log.d("task_details", "date: " + taskDate);
                Log.d("task_details", "taskId: " + taskId);

                startActivity(editIntent);
            }
        });

        Button deleteButton = findViewById(R.id.Delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = getIntent().getStringExtra("uid");
                String taskId = getIntent().getStringExtra("taskId");

                if (uid != null && taskId != null) {
                    Log.d("DeleteTask", "Deleting task with ID: " + taskId + " for UID: " + uid);
                    deleteTask(uid, taskId);

                    reloadCounter = 1;


                } else {
                    Log.e("DeleteTask", "UID or Task ID is null");
                }

                Intent intent = new Intent(taskDetailsActivity.this, homepage.class);
                intent.putExtra("reloadCounter", reloadCounter);
                reloadCounter = 0;
                startActivity(intent);
                finish();
            }
        });


    }

    private void deleteTask(String uid, String taskId) {
        Log.d("DeleteTask", "Deleting task with ID: " + taskId + " for UID: " + uid);

        if (uid != null && !uid.isEmpty() && taskId != null && !taskId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference tasksCollection = db.collection("UserTask");

            Query query = tasksCollection.whereEqualTo("uid", uid).whereEqualTo("taskId", taskId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirestoreDelete", "Task successfully deleted");

                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreDelete", "Error deleting document: " + e.getMessage(), e);
                                });
                    }
                } else {
                    Log.e("FirestoreDelete", "Error getting documents: " + task.getException());
                }
            });
        } else {
            Log.e("FirestoreDelete", "UID or Task ID is null or empty");
        }
    }
}