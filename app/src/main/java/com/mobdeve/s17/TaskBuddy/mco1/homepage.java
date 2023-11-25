package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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

    TextView sort_info_text;
    List<task_rv> task_rvList = new ArrayList<>();

    private int currentSortOption = R.id.sort_by_letter;  // Default sorting option
    private int currentSortOrder = R.id.sort_asc;  //Default sorting order


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
        sort_info_text = findViewById(R.id.sort_info_text);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid");
        } else {
            finish();
        }
        Intent getData = getIntent();
        if (getData != null) {
            String taskName = getData.getStringExtra("taskName");
            String description = getData.getStringExtra("description");
            String date = getData.getStringExtra("date");
            String status = getData.getStringExtra("status");
            String priority = getData.getStringExtra("priority");
            String imageUrl = getData.getStringExtra("imageUrl");
            String uid = getData.getStringExtra("uid");
            String taskId = getData.getStringExtra("taskId");


            Task newTask = new Task(taskName, description, date, status, priority, imageUrl, uid, taskId);
            updateRecyclerViewWithNewTask(newTask);

        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view);

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
                                    currentSortOption=R.id.sort_by_letter;
                                    currentSortOrder = subMenuItem.getItemId();

                                    sortTasks(currentSortOption, currentSortOrder);

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
                                    currentSortOption=R.id.sort_by_priority;
                                    currentSortOrder = subMenuItem.getItemId();
                                    // Perform sorting
                                    sortTasks(currentSortOption, currentSortOrder);
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
                                    currentSortOption=R.id.sort_by_status;
                                    currentSortOrder = subMenuItem.getItemId();
                                    // Perform sorting
                                    sortTasks(currentSortOption, currentSortOrder);

                                    return true;
                                }
                            });

                            subMenu.show();
                            return true;
                        }else if (itemId == R.id.sort_by_date) {
                            PopupMenu subMenu = new PopupMenu(homepage.this, view);
                            subMenu.getMenuInflater().inflate(R.menu.sub_menu_date, subMenu.getMenu());

                            subMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem subMenuItem) {
                                    currentSortOption=R.id.sort_by_date;
                                    currentSortOrder = subMenuItem.getItemId();
                                    // Perform sorting
                                    sortTasks(currentSortOption, currentSortOrder);
                                    return true;
                                }
                            });

                            subMenu.show();
                            return true;}

                        return false;
                    }
                });

                popupmenu.show();
            }
        });

        updateRecyclerView(uid);

    }
    task_rv deletedTask = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("Swiped", "onSwiped method called");

            try {
                int position = viewHolder.getAbsoluteAdapterPosition();
                switch(direction){
                    case ItemTouchHelper.LEFT:
                        if (position >= 0 && position < task_rvList.size()) {
                            deletedTask = task_rvList.get(position);
                            Log.d("TaskID", "Task ID to delete: " + deletedTask.getTaskId());

                            task_rvList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.deleteItem(position);

                            deleteTaskFromFirestore(deletedTask.getTaskId());

                            String deletedTaskName = deletedTask.getName();
                            Snackbar.make(recycler_view, "Deleted task: " + deletedTaskName, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            task_rvList.add(position, deletedTask);
                                            adapter.notifyItemInserted(position);
                                        }
                                    }).show();
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e("Swiped", "Error in onSwiped", e);
            }


        }
    };

    private void deleteTaskFromFirestore(String taskId) {
        Log.d("FirestoreDelete", "Deleting task with ID: " + taskId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tasksCollection = db.collection("UserTask");
        tasksCollection.document(taskId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirestoreDelete", "Task deleted successfully from Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreDelete", "Error deleting task from Firestore", e);
                    }
                });
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
                                    document.getString("date"),
                                    document.getString("description"),
                                    document.getString("imageUrl"),
                                    document.getString("uid"),
                                    document.getString("taskId")
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
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Homepage", "onResume called");
        updateRecyclerView(uid);
    }


    private void updateRecyclerView(String uid) {
        getList(uid);
    }
    private void updateRecyclerViewWithNewTask(Task newTask) {
        List<task_rv> taskList = adapter.getTaskList();

        task_rv newTask_rv = new task_rv(newTask.taskName, newTask.priority, newTask.status, newTask.date, newTask.description, newTask.imageUrl, newTask.getTaskId(), uid);

        taskList.add(newTask_rv);

        adapter.notifyDataSetChanged();
    }
    private void sortTasks(int sortOption, int sortOrder) {

        List<task_rv> taskList = adapter.getTaskList();
        Log.d("SortTasks", "Sort Option: " + sortOption + ", Sort Order: " + sortOrder);
        sort_info_text = findViewById(R.id.sort_info_text);
        sort_info_text.setText("Sorted by: ");


        if (sortOption == R.id.sort_by_letter) {
            // Sort by A-Z
            sort_info_text.append("Task Name, Order: " + (sortOrder == R.id.sort_asc ? "A-Z" : "Z-A"));
            Collections.sort(taskList, new TaskNameComparator());
        } else if (sortOption == R.id.sort_by_priority) {
            // Sort by Priority
            sort_info_text.append("Priority, Order: " + (sortOrder == R.id.sort_asc ? "Low to High" : "High to Low"));
            // Sort by Priority
            Collections.sort(taskList, new PriorityComparator());
        } else if (sortOption == R.id.sort_by_status) {
            // Sort by Status
            sort_info_text.append("Status, Order: " + (sortOrder == R.id.sort_asc ? "Completed to Not Done" : "Not Done to Completed"));
            Collections.sort(taskList, new StatusComparator());
        }
        else if (sortOption == R.id.sort_by_date) {
            // Sort by Due Date
            sort_info_text.append("Due Date, Order: " + (sortOrder == R.id.sort_asc ? "Earliest to Latest" : "Latest to Earliest"));
            Collections.sort(taskList, new DueDateComparator());
        }

        // Check the sort order and reverse the list if descending
        if (sortOrder == R.id.sort_desc) {
            Collections.reverse(taskList);
        }

        // Log the sorted task list for debugging
        for (task_rv task : taskList) {
            Log.d("SortedTask", task.getName() + " - Priority: " + task.getPriority() + ", Status: " + task.getStatus());
        }

        adapter.notifyDataSetChanged();
    }


    private static class TaskNameComparator implements Comparator<task_rv> {
        @Override
        public int compare(task_rv task1, task_rv task2) {
            return task1.getName().compareToIgnoreCase(task2.getName());
        }
    }

    private static class PriorityComparator implements Comparator<task_rv> {
        @Override
        public int compare(task_rv task1, task_rv task2) {
            List<String> priorityOrder = Arrays.asList("LOW", "MEDIUM", "HIGH");

            String priority1 = task1.getPriority();
            String priority2 = task2.getPriority();

            return Integer.compare(priorityOrder.indexOf(priority1), priorityOrder.indexOf(priority2));
        }
    }

    private static class StatusComparator implements Comparator<task_rv> {
        @Override
        public int compare(task_rv task1, task_rv task2) {
            List<String> statusOrder = Arrays.asList("COMPLETED", "IN PROGRESS", "NOT DONE");


            String status1 = task1.getStatus();
            String status2 = task2.getStatus();

            return Integer.compare(statusOrder.indexOf(status1), statusOrder.indexOf(status2));
        }
    }

    private static class DueDateComparator implements Comparator<task_rv> {
        @Override
        public int compare(task_rv task1, task_rv task2) {
            return task1.getDate().compareTo(task2.getDate());
        }
    }











}