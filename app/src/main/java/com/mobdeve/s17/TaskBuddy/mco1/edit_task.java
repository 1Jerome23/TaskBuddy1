package com.mobdeve.s17.TaskBuddy.mco1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class edit_task extends AppCompatActivity {

    EditText edit_name;
    EditText edit_due;
    EditText edit_details;
    Spinner spinner;
    Spinner spinner2;
    ImageView edit_file;
    Button confirm_edit;
    ImageButton edit_profile;
    ImageButton closeButton;
    Button attachFileButton;

    private int position;
    private task_rv selectedTask;
    private task_adapter adapter;
    private static final int FILE_PICKER_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        edit_name = findViewById(R.id.edit_name);
        edit_due = findViewById(R.id.edit_due);
        edit_details = findViewById(R.id.edit_details);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        edit_file = findViewById(R.id.edit_file);
        confirm_edit = findViewById(R.id.confirm_edit);
        edit_profile = findViewById(R.id.edit_profile);
        closeButton = findViewById(R.id.closeButton);
        attachFileButton = findViewById(R.id.attachFileButton);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        String uid = intent.getStringExtra("uid");

        String taskName = intent.getStringExtra("taskName");
        String taskDate = intent.getStringExtra("taskDate");
        String taskStatus = intent.getStringExtra("taskStatus");
        String taskPriority = intent.getStringExtra("taskPriority");
        String description = intent.getStringExtra("description");
        String imageURL = intent.getStringExtra("imageUrl");

        edit_name.setText(taskName);
        edit_due.setText(taskDate);
        edit_details.setText(description);

        setSpinnerSelection(spinner, taskStatus);
        setSpinnerSelection(spinner2, taskPriority);

        if (imageURL != null && !imageURL.isEmpty()) {
            Picasso.get().load(imageURL).into(edit_file, new Callback() {
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
            edit_file.setVisibility(View.GONE);
        }

        String[] priorityItems = {"HIGH", "MEDIUM", "LOW"};
        String[] statusItems = {"NOT DONE", "IN PROGRESS", "COMPLETED"};

        int[] colors = {Color.parseColor("#FF7165"), Color.parseColor("#FFE59C"), Color.parseColor("#A2EF87")};

        add_task addTaskInstance = new add_task(); // Create an instance of add_task
        add_task.CustomSpinnerAdapter priorityAdapter = addTaskInstance.new CustomSpinnerAdapter(edit_task.this, R.layout.spinner_item, priorityItems, colors);
        add_task.CustomSpinnerAdapter statusAdapter = addTaskInstance.new CustomSpinnerAdapter(edit_task.this, R.layout.spinner_item, statusItems, colors);

        priorityAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        spinner.setAdapter(priorityAdapter);
        spinner2.setAdapter(statusAdapter);

        confirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = edit_name.getText().toString();
                String date = edit_due.getText().toString();
                String description = edit_details.getText().toString();
                String priority = spinner.getSelectedItem().toString();
                String status = spinner2.getSelectedItem().toString();

                String taskId = getIntent().getStringExtra("taskId");
                if (taskId == null || taskId.isEmpty()) {
                    return;
                }

                task_rv updatedTask = new task_rv(
                        taskName,
                        priority,
                        status,
                        date,
                        description,
                        imageURL,
                        uid,
                        taskId
                );

                updateTaskInFirestore(updatedTask, uid,taskId );
                Intent resultIntent = new Intent(edit_task.this, homepage.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(resultIntent);

                finish();
            }
        });
//        attachFileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFilePicker();
//            }
//        });

        edit_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDatePickerDialog();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    Intent intent = new Intent(edit_task.this, profile.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }

        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImageUrl();
            }
        });
        closeButton.setVisibility(imageURL != null && !imageURL.isEmpty() ? View.VISIBLE : View.GONE);

    }
    private void removeImageUrl() {
        String imageUrl = "";
        loadOrUpdateImage(imageUrl);
        closeButton.setVisibility(imageUrl != null && !imageUrl.isEmpty() ? View.VISIBLE : View.GONE);

        task_rv updatedTask = new task_rv(
                edit_name.getText().toString(),
                spinner.getSelectedItem().toString(),
                spinner2.getSelectedItem().toString(),
                edit_due.getText().toString(),
                edit_details.getText().toString(),
                imageUrl,
                getIntent().getStringExtra("uid"),
                getIntent().getStringExtra("taskId")
        );

        updateTaskInFirestore(updatedTask,getIntent().getStringExtra("uid"), getIntent().getStringExtra("taskId"));
    }

    private void loadOrUpdateImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(edit_file);
        } else {
            edit_file.setVisibility(View.GONE);
        }
    }
    private void updateTaskInFirestore(task_rv updatedTask, String uid, String taskId) {

        if (uid != null && !uid.isEmpty() && taskId != null && !taskId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference tasksCollection = db.collection("UserTask");

            Query query = tasksCollection.whereEqualTo("uid", uid).whereEqualTo("taskId", taskId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().set(updatedTask.toMap(), SetOptions.merge())
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirestoreUpdate", "Task updated successfully");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreUpdate", "Error updating task: " + e.getMessage(), e);
                                });
                    }
                } else {
                    Log.e("FirestoreUpdate", "Error getting documents: " + task.getException());
                }
            });
        } else {
            Log.e("FirestoreUpdate", "UID or Task ID is null or empty");
        }
    }

//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
//    }

    private void setSpinnerSelection(Spinner spinner, String selectedItem) { //getting the spinner items
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(selectedItem)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    private void showEditDatePickerDialog() { //for getting date in calendar
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String selectedDate = String.format("%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                    edit_due.setText(selectedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        datePickerDialog.show();
    }





}
