package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class taskDetailsActivity extends AppCompatActivity {

    TextView task_name;
    TextView task_description;
    TextView  task_due;
    TextView task_date;
    TextView task_textStatus;
    TextView task_status;
    TextView task_duepriority;
    TextView task_priority;
    TextView task_attachment;
    ImageView add_file;
    Button Back_button;
    Button Edit_button;
    Button Delete_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);

        // Retrieve task details from the Intent
        Intent intent = getIntent();
        String taskName = intent.getStringExtra("task_name");
        String taskDate = intent.getStringExtra("task_date");
        String taskStatus = intent.getStringExtra("task_status");
        String taskPriority = intent.getStringExtra("task_priority");

        // Populate the layout with task details
        TextView nameTextView = findViewById(R.id.task_name);
        TextView dateTextView = findViewById(R.id.task_date);
        TextView statusTextView = findViewById(R.id.task_status);
        TextView priorityTextView = findViewById(R.id.task_priority);

        nameTextView.setText(taskName);
        dateTextView.setText(taskDate);
        statusTextView.setText(taskStatus);
        priorityTextView.setText(taskPriority);

        Button Back_button = findViewById(R.id.Back_button);
        Back_button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (taskDetailsActivity.this,homepage.class);
                startActivity(intent);
            }
        });
//        Button Delete_button = findViewById(R.id.Delete_button);
//        Back_button.setOnClickListener(new View.OnClickListener(){
//
//        });

//        Button Edit_button = findViewById(R.id.Edit_button){
//            Edit_button.setOnClickListener(new View.OnClickListener(){
//
//
//            });
//        }
    }
}
