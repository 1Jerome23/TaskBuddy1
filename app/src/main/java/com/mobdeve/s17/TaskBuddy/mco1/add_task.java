package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class add_task extends AppCompatActivity {
    private static final int FILE_PICKER_REQUEST_CODE = 2; // Define your request code for file picker
    private static final int CAMERA_REQUEST_CODE = 3; // Define your request code for camera capture
    private static final int REQUEST_CODE = 2; // Define your request code

    ImageView add_icon;
    TextView add_create;
    TextView add_title;
    EditText add_name;
    Spinner spinner;
    Spinner spinner2;
    TextView add_due_date;
    EditText add_due;
    TextView add_text_detail;
    EditText add_details;
    TextView add_attachments;
    ImageView imageView;
    Button attachFileButton;
    Button add_confirm;
    ImageView add_footer;
    ImageButton add_homepage;
    ImageButton add_profile;
    ImageButton add_calendar;
    TextView add_priority_task;
    TextView add_status_task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        ImageView add_icon = findViewById(R.id.add_icon);
        TextView add_create = findViewById(R.id.add_create);
        TextView add_title = findViewById(R.id.add_title);
        EditText add_name = findViewById(R.id.add_name);
        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner2 = findViewById(R.id.spinner2);
        TextView add_due_date = findViewById(R.id.add_due_date);
        EditText add_due = findViewById(R.id.add_due);
        TextView add_text_detail = findViewById(R.id.add_text_detail);
        EditText add_details = findViewById(R.id.add_details);
        TextView add_attachments = findViewById(R.id.add_attachments);
        ImageView imageView = findViewById(R.id.add_file);
        Button attachFileButton = findViewById(R.id.attachFileButton);
        Button add_confirm = findViewById(R.id.add_confirm);
        ImageView add_footer = findViewById(R.id.add_footer);
        ImageButton add_homepage = findViewById(R.id.add_homepage);
        ImageButton add_profile = findViewById(R.id.add_profile);

        add_homepage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (add_task.this,homepage.class);
                startActivity(intent);
            }
        });
        add_profile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent (add_task.this,profile.class);
                startActivity(intent);
            }
        });


        String[] items = {"HIGH", "MEDIUM", "LOW"};
        int[] colors = {Color.parseColor("#FF7165"), Color.parseColor("#FFE59C"), Color.parseColor("#A2EF87")};

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, items, colors);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String[] statusItems = {"NOT DONE", "IN PROGRESS", "COMPLETED"};
        CustomSpinnerAdapter statusAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, statusItems, colors);
        statusAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner2.setAdapter(statusAdapter);

        attachFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapter.setSelectedItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                statusAdapter.setSelectedItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

    }

    // Create a custom adapter class
    public class CustomSpinnerAdapter extends ArrayAdapter<String> {

        private int[] colors;
        private int selectedItem = -1; // Variable to track the selected item

        public CustomSpinnerAdapter(Context context, int resource, String[] items, int[] colors) {
            super(context, resource, items);
            this.colors = colors;
        }

        @NonNull
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);

            // Set the background color based on the position
            if (position >= 0 && position < colors.length) {
                v.setBackgroundColor(colors[position]);
            } else {
                // Set the background color to the default color if position is out of bounds
                v.setBackgroundColor(Color.WHITE);
            }
            return v;
        }
        public void setSelectedItem(int position) {
            // Set the selected item and refresh the adapter
            selectedItem = position;
            notifyDataSetChanged();
        }
    }



    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            // Handle the selected file URI
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured image
            Bitmap photo = (Bitmap) data.getExtras().get("data");
        }
    }


}


