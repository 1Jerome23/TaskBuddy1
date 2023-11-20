package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

public class edit_task extends AppCompatActivity {

    EditText edit_name;
    EditText edit_due;
    EditText edit_details;
    Spinner spinner;
    Spinner spinner2;
    TextView edit_file;
    Button edit_confirm;

    private int position;
    private task_rv selectedTask; // Add this line to store the selected task
    private task_adapter adapter; // Add this line to store the adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedTask")) {
            task_rv selectedTask = intent.getParcelableExtra("selectedTask");
            position = intent.getIntExtra("position", -1); 

            edit_name = findViewById(R.id.edit_name);
            edit_due = findViewById(R.id.edit_due);
            edit_details = findViewById(R.id.edit_details);
            spinner = findViewById(R.id.spinner);
            spinner2 = findViewById(R.id.spinner2);
            edit_file = findViewById(R.id.edit_file);
            edit_confirm = findViewById(R.id.edit_confirm);

            edit_name.setText(selectedTask.getName());
            edit_due.setText(selectedTask.getDate());
            edit_details.setText(selectedTask.getDescription());

            String priorityLevel = selectedTask.getPriority();

            if ("HIGH".equals(priorityLevel)) {
                spinner.setSelection(0);
            } else if ("MEDIUM".equals(priorityLevel)) {
                spinner.setSelection(1);
            } else if ("LOW".equals(priorityLevel)) {
                spinner.setSelection(2);
            } else {
                spinner.setSelection(0);
            }

            String taskStatus = selectedTask.getStatus();

            if ("NOT DONE".equals(taskStatus)) {
                spinner2.setSelection(0);
            } else if ("IN PROGRESS".equals(taskStatus)) {
                spinner2.setSelection(1);
            } else if ("COMPLETED".equals(taskStatus)) {
                spinner2.setSelection(2);
            } else {
                spinner2.setSelection(0);
            }

        }

        edit_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = edit_name.getText().toString();
                String updatedDue = edit_due.getText().toString();
                String updatedDetails = edit_details.getText().toString();
                String updatedPriority = spinner.getSelectedItem().toString();
                String updatedStatus = spinner2.getSelectedItem().toString();

                task_rv updatedTask = new task_rv(
                        updatedName,
                        updatedPriority,
                        updatedStatus,
                        updatedDue,
                        updatedDetails,
                        selectedTask.getImageUrl()
                );

                List<task_rv> taskList = adapter.getTaskList();

                if (position != -1 && position < taskList.size()) {
                    taskList.set(position, updatedTask);
                }

                adapter.notifyDataSetChanged();
                finish();
            }
        });
    }
}
