package com.mobdeve.s17.TaskBuddy.mco1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.SearchView;
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
import java.util.Locale;

public class homepage extends AppCompatActivity {
    int menuResourceId = R.menu.sort_menu;

    RecyclerView recycler_view;
    task_adapter adapter;
    ImageView homepage_icon;
    TextView homepage_home;
    ImageButton homepage_add;
    Button homepage_sort;
    Button homepage_start;
    Button homepage_end;
    ImageView homepage_footer;
    ImageButton homepage_homepage;
    ImageButton homepage_profile;
    private String uid = "";

    TextView sort_info_text;
    List<task_rv> task_rvList = new ArrayList<>();
    List<task_rv> filteredList = new ArrayList<>();
    private String startDate = null;
    private String endDate = null;


    private int currentSortOption = R.id.sort_by_letter;  // Default sorting option
    private int currentSortOrder = R.id.sort_asc;  //Default sorting order

    private SearchView homepage_search1;
    private String previousSearchText = "";
    //private boolean reloadNeeded = false;
    int reloadCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        recycler_view = findViewById(R.id.recycler_view);
        setRecyclerView(new ArrayList<>());

        homepage_icon = (ImageView) findViewById(R.id.homepage_icon);
        homepage_home = (TextView) findViewById(R.id.homepage_home);
        homepage_add = (ImageButton) findViewById(R.id.homepage_add);
        //homepage_search = (EditText) findViewById(R.id.homepage_search);
        homepage_search1 = (SearchView) findViewById(R.id.homepage_search1);
        homepage_search1.clearFocus();
        homepage_search1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()==0){
                    setRecyclerView(task_rvList);
                }
                else if (newText.length() < previousSearchText.length()){
                    // Backspace detected, update the list
                    filterList(newText);
                }
                else
                {
                    //no backspace
                filterList(newText);
                }

                // Update the previous search text
                previousSearchText = newText;
                return true;

            }
        });



        homepage_sort = (Button) findViewById(R.id.homepage_sort);

        homepage_footer = (ImageView) findViewById(R.id.homepage_footer);
        homepage_homepage = (ImageButton) findViewById(R.id.homepage_homepage);
        homepage_profile = (ImageButton) findViewById(R.id.homepage_profile);
        sort_info_text = findViewById(R.id.sort_info_text);
        homepage_start = findViewById(R.id.homepage_start);
        homepage_end = findViewById(R.id.homepage_end);

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

        //reload Activity once a delete is made
        reloadAct();

        homepage_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(homepage_start);
            }
        });

        homepage_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(homepage_end);
            }
        });

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

        updateRecyclerView(uid,null,null);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view);

    }
    private boolean isSwiping = false;
    task_rv deletedTask = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
//aa
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            try {
                int position = viewHolder.getAbsoluteAdapterPosition();

                if (position >= 0 && position < task_rvList.size()) {
                    deletedTask = task_rvList.get(position);

                    deleteTaskFromFirestore(deletedTask.getTaskId(), position);
                } else {
                }
            } catch (Exception e) {
                Log.e("Swiped", "Error in onSwiped", e);
                Log.e("AppCrash", "Error in deleteTaskFromFirestore", e);
            } finally {
                isSwiping = false;
            }


    }
        private void deleteTaskFromFirestore(String taskId, int position) {
            Log.d("FirestoreDelete", "Deleting task with ID: " + taskId + " at position: " + position);

            if (taskId != null && !taskId.isEmpty()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference tasksCollection = db.collection("UserTask");

                Query query = tasksCollection.whereEqualTo("taskId", taskId);

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("FirestoreDelete", "Task successfully deleted");
                                        removeFromListAndNotify(position);                     })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirestoreDelete", "Error deleting document: " + e.getMessage(), e);
                                    });
                        }
                    } else {
                        Log.e("FirestoreDelete", "Error getting documents: " + task.getException());
                    }
                });
            } else {
                Log.e("FirestoreDelete", "Task ID is null or empty");
            }
        }

    };
    private void removeFromListAndNotify(int position) {
        if (isValidPosition(position)) {
            task_rvList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, task_rvList.size()); // Add this line
        } else {
            Log.d("RemoveFromList", "Invalid position: " + position);
        }
    }
    private boolean isValidPosition(int position) {
        return position >= 0 && position < task_rvList.size();
    }

    private void getList(String uid, String startDate, String endDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("GetList", "UID: " + uid + ", Start Date: " + startDate + ", End Date: " + endDate);
        db.collection("UserTask")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    task_rvList.clear();
                    filteredList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
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
                            filteredList.add(taskRv);
                        } else {
                            Log.e("FirestoreError", "Document is missing expected fields");
                        }
                    }

                    if (task_rvList.isEmpty()) {
                    } else {
                        setRecyclerView(task_rvList);
                        filterTasksByDates();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(homepage.this, "Error querying Firestore", Toast.LENGTH_SHORT).show();
                });
    }
    private void filterTasksByDates() {
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            adapter.filterByDates(startDate, endDate);
        }
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
        updateRecyclerView(uid,null,null);

    }


    private void updateRecyclerView(String uid, String startDate, String endDate) {
        getList(uid, startDate, endDate);
        adapter.notifyDataSetChanged();
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
    private void showDatePickerDialog(final Button button) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear += 1;

            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", monthOfYear, dayOfMonth, year);
            button.setText(selectedDate);

            if (button == homepage_start) {
                startDate = selectedDate;
            } else if (button == homepage_end) {
                endDate = selectedDate;
            }
            updateRecyclerView(uid, startDate, endDate);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
                java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
                java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void filterList(String text) {
        List<task_rv> filteredList = new ArrayList<>();
        List<task_rv> fullList = adapter.getTaskList(); // Store the full list of tasks

        if (text.isEmpty()) {
            // If the search text is empty, show all tasks
            setRecyclerView(fullList);
        } else {
            // Filter the list based on the search text
            for (task_rv task : fullList) {
                if (task.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(task);
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }
            setRecyclerView(filteredList);
        }
    }

    private void reloadAct(){
        Intent intent = new Intent(homepage.this, homepage.class);
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            reloadCounter = extras.getInt("reloadCounter");
            Log.e("checkreload", "reloadAct engaging");
        }

        //a delete is made
        if(reloadCounter == 1){
            Log.e("checkreload", "reloadCounter: " + reloadCounter );
            reloadCounter = 0;
            Log.e("checkreload", "updated Counter: " + reloadCounter );
            this.recreate();

        }


    }


}