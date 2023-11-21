package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

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
    private String uid = "";

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
            Log.d("CHECKER", "Received uid: " + uid);
            Log.d("CHECKER", "Received taskId: " + taskId);

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
            Picasso.get().load(imageURL).into(imageView);

        }

        Button Back_button = findViewById(R.id.Back_button);
        Button Edit_button = findViewById(R.id.Edit_button);

        Back_button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (taskDetailsActivity.this,homepage.class);
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
                editIntent.putExtra("uid",uid);
                Log.d("task_details", "uid: " + uid);
                Log.d("task_details", "taskName: " + taskName);
                Log.d("task_details", "description: " + description);
                Log.d("task_details", "date: " + taskDate);
                Log.d("task_details", "taskId: " + taskId);

                startActivity(editIntent);
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
