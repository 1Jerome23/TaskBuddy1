package com.mobdeve.s17.TaskBuddy.mco1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class add_task extends AppCompatActivity {
    private static final int FILE_PICKER_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    private static final int REQUEST_CODE = 2;

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
    Bitmap bitmap;
    private String savedSelectedDate;


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
        add_due = findViewById(R.id.add_due);
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
        add_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        add_due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("UserTask");

        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user's email
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();

                    String taskName = add_name.getText().toString();
                    String date = add_due_date.getText().toString();
                    String description = add_details.getText().toString();
                    String status = add_status_task.getText().toString();
                    String priority = add_priority_task.getText().toString();
                    String imageKey = taskName + "_" + System.currentTimeMillis() + ".jpg";
                    uploadImageToStorage(imageKey, bitmap);

                    Task task = new Task(taskName, date, description, status, priority, imageKey, userEmail);

                    myRef.child(userEmail).push().setValue(task);
                }
            }
        });
    }
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
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured image
            Bitmap photo = (Bitmap) data.getExtras().get("data");
        }
    }
    private void uploadImageToStorage(String imageKey, Bitmap bitmap) {
        // Convert the Bitmap to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageKey);

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        String imageUrl = downloadUri.toString();

                    }
                });
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    // Handle the selected date
                    String selectedDate = String.format("%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                    add_due.setText(selectedDate);
                } catch (Exception e) {
                    Log.e("DatePicker", "Error setting date: " + e.getMessage());
                    e.printStackTrace();

                }
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        // Show the dialog
        datePickerDialog.show();
    }
    private void saveSelectedDate(String selectedDate) {
        savedSelectedDate = selectedDate;
    }


}


