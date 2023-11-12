package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class homepage extends AppCompatActivity {
    int menuResourceId = R.menu.sort_menu;

    RecyclerView recycler_view;
    task_adapter adapter;
    ImageView homepage_icon;
    TextView homepage_home;
    ImageButton homepage_add;
    EditText homepage_search;
    Button homepage_sort;
    TextView homepage_name;
    TextView homepage_priority;
    TextView homepage_status;
    TextView homepage_date;
    ImageView homepage_footer;
    ImageButton homepage_homepage;
    ImageButton homepage_profile;
    private String uid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        recycler_view = findViewById(R.id.recycler_view);
        setRecyclerView(new ArrayList<>());

        homepage_icon = (ImageView) findViewById(R.id.homepage_icon);
        homepage_home = (TextView) findViewById(R.id.homepage_home);
        homepage_add = (ImageButton) findViewById(R.id.homepage_add);
        homepage_search = (EditText) findViewById(R.id.homepage_search);
        homepage_sort = (Button) findViewById(R.id.homepage_sort);


        homepage_footer = (ImageView) findViewById(R.id.homepage_footer);
        homepage_homepage = (ImageButton) findViewById(R.id.homepage_homepage);
        homepage_profile = (ImageButton) findViewById(R.id.homepage_profile);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid");
        } else {
            finish();
        }

        homepage_profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, profile.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });


        homepage_add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, add_task.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        homepage_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupmenu = new PopupMenu(homepage.this, view);
                popupmenu.getMenuInflater().inflate(R.menu.sort_menu, popupmenu.getMenu());

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.sort_by_letter) {
                            PopupMenu subMenu = new PopupMenu(homepage.this, view);
                            subMenu.getMenuInflater().inflate(R.menu.sub_menu_letter, subMenu.getMenu());

                            subMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem subMenuItem) {
                                    return true;
                                }
                            });

                            subMenu.show();
                            return true;
                        } else if (itemId == R.id.sort_by_priority) {
                            PopupMenu subMenu = new PopupMenu(homepage.this, view);
                            subMenu.getMenuInflater().inflate(R.menu.sub_menu_priority, subMenu.getMenu());

                            subMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem subMenuItem) {
                                    return true;
                                }
                            });

                            subMenu.show();
                            return true;
                        } else if (itemId == R.id.sort_by_status) {
                            PopupMenu subMenu = new PopupMenu(homepage.this, view);
                            subMenu.getMenuInflater().inflate(R.menu.sub_menu_status, subMenu.getMenu());

                            subMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem subMenuItem) {
                                    return true;
                                }
                            });

                            subMenu.show();
                            return true;
                        }

                        return false;
                    }
                });

                popupmenu.show();
            }
        });

        updateRecyclerView(uid);

    }
    private void getList(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<task_rv> task_rvList = new ArrayList<>();

        db.collection("UserTask")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("FirestoreQuery", "Querying Firestore for UID: " + uid);

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d("DocData", document.getId() + " => " + document.getData());

                        if (document.contains("taskName") && document.contains("priority")
                                && document.contains("status") && document.contains("date")) {
                            task_rv taskRv = new task_rv(
                                    document.getString("taskName"),
                                    document.getString("priority"),
                                    document.getString("status"),
                                    document.getString("date")
                            );
                            task_rvList.add(taskRv);
                        } else {
                            Log.e("FirestoreError", "Document is missing expected fields");
                        }
                    }

                    if (task_rvList.isEmpty()) {
                    } else {
                        setRecyclerView(task_rvList);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(homepage.this, "Error querying Firestore", Toast.LENGTH_SHORT).show();
                });
    }


    private void setRecyclerView(List<task_rv> task_rvList) {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new task_adapter(this, task_rvList);
        recycler_view.setAdapter(adapter);
    }


    private void updateRecyclerView(String uid) {
        getList(uid);
    }



}