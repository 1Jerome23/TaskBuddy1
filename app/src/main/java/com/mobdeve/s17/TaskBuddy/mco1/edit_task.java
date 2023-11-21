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
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class edit_task extends AppCompatActivity {

    EditText edit_name;
    EditText edit_due;
    EditText edit_details;
    Spinner spinner;
    Spinner spinner2;
    ImageView edit_file; // Change TextView to ImageView
    Button confirm_edit;

    private int position;
    private task_rv selectedTask;
    private task_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        // Initialize UI elements
        edit_name = findViewById(R.id.edit_name);
        edit_due = findViewById(R.id.edit_due);
        edit_details = findViewById(R.id.edit_details);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        edit_file = findViewById(R.id.edit_file);
        confirm_edit = findViewById(R.id.confirm_edit);


        // Receive data from Intent
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String taskId = intent.getStringExtra("taskId");
        String uid = intent.getStringExtra("uid");
        Log.d("EditTask", "Received uid: " + uid);
        Log.d("EditTask", "Received taskId: " + taskId);

        // Retrieve data from the Intent
        String taskName = intent.getStringExtra("taskName");
        String taskDate = intent.getStringExtra("taskDate");
        String taskStatus = intent.getStringExtra("taskStatus");
        String taskPriority = intent.getStringExtra("taskPriority");
        String description = intent.getStringExtra("description");
        String imageURL = intent.getStringExtra("imageUrl");

        // Populate UI elements with data from the Intent
        edit_name.setText(taskName);
        edit_due.setText(taskDate);
        edit_details.setText(description);

        // Set the selected item in the spinner
        setSpinnerSelection(spinner, taskStatus);
        setSpinnerSelection(spinner2, taskPriority);
        Log.d("Spinner", "Task Priority: " + taskPriority);
        Log.d("Spinner", "Task Status: " + taskStatus);

        // Load image using Picasso (assuming you have a URL for the image)
        Picasso.get().load(imageURL).into(edit_file);
        String[] priorityItems = {"HIGH", "MEDIUM", "LOW"};
        String[] statusItems = {"NOT DONE", "IN PROGRESS", "COMPLETED"};

        int[] colors = {Color.parseColor("#FF7165"), Color.parseColor("#FFE59C"), Color.parseColor("#A2EF87")};

        add_task addTaskInstance = new add_task(); // Create an instance of add_task
        add_task.CustomSpinnerAdapter priorityAdapter = addTaskInstance.new CustomSpinnerAdapter(edit_task.this, R.layout.spinner_item, priorityItems, colors);
        add_task.CustomSpinnerAdapter statusAdapter = addTaskInstance.new CustomSpinnerAdapter(edit_task.this, R.layout.spinner_item, statusItems, colors);


        // Set the dropdown view resource for both adapters
        priorityAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        // Set adapters to spinners
        spinner.setAdapter(priorityAdapter);
        spinner2.setAdapter(statusAdapter);



        confirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated information from the UI elements
                String taskName = edit_name.getText().toString();
                String date = edit_due.getText().toString();
                String description = edit_details.getText().toString();
                String priority = spinner.getSelectedItem().toString();
                String status = spinner2.getSelectedItem().toString();

                Log.d("EditTask", "Updated Name: " + taskName);
                Log.d("EditTask", "Updated Due Date: " + date);
                Log.d("EditTask", "Updated Details: " + description);
                Log.d("EditTask", "Updated Priority: " + priority);
                Log.d("EditTask", "Updated Status: " + status);
                Log.d("EditTask", "Updated id: " + taskId);
                Log.d("EditTask", "Updated uid: " + uid);
                Log.d("EditTask", "Updated Status: " + imageURL);

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

                updateTaskInFirestore(updatedTask, taskId);
                Intent intent = new Intent(edit_task.this, homepage.class);
                startActivity(intent);

                finish();
            }
        });

        edit_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDatePickerDialog();
            }
        });
    }
    private void updateTaskInFirestore(task_rv updatedTask, String taskId) {
        Log.d("EditTask", "Updating task in Firestore");

        if (taskId != null && !taskId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference tasksCollection = db.collection("UserTask");

            DocumentReference taskDocument = tasksCollection.document(taskId);

            taskDocument.set(updatedTask.toMap(), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirestoreUpdate", "Task updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreUpdate", "Error updating task: " + e.getMessage());
                    });

        } else {
            Log.e("FirestoreUpdate", "Task ID is null or empty");
        }
    }

    private void setSpinnerSelection(Spinner spinner, String selectedItem) {
        Log.d("Spinner", "Setting selection to: " + selectedItem);
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(selectedItem)) {
                spinner.setSelection(i);
                Log.d("Spinner", "Selection set successfully");
                break;
            }
        }
    }
    private void showEditDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String selectedDate = String.format("%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                    edit_due.setText(selectedDate);
                } catch (Exception e) {
                    Log.e("DatePicker", "Error setting date: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        // Use the current date as the default date in the picker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog with the current date and the dateSetListener
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }





}
