package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        recycler_view = findViewById(R.id.recycler_view);
        setRecyclerView();

        homepage_icon = (ImageView) findViewById(R.id.homepage_icon);
        homepage_home = (TextView) findViewById(R.id.homepage_home);
        homepage_add = (ImageButton) findViewById(R.id.homepage_add);
        homepage_search = (EditText) findViewById(R.id.homepage_search);
        homepage_sort = (Button) findViewById(R.id.homepage_sort);


        homepage_footer = (ImageView) findViewById(R.id.homepage_footer);
        homepage_homepage = (ImageButton) findViewById(R.id.homepage_homepage);
        homepage_profile = (ImageButton) findViewById(R.id.homepage_profile);


        homepage_profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, profile.class);
                startActivity(intent);
            }
        });


        homepage_add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, add_task.class);
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


    }
        private void setRecyclerView(){
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new task_adapter(this,getList());
        recycler_view.setAdapter(adapter);

    }


    private List<task_rv>getList(){
        List<task_rv> task_rvList = new ArrayList<>();
        task_rvList.add(new task_rv("ITNET HOMEWORKSSSSSSS12345", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("MOBDEVE HOMEWORK", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("ITSRAQA HOMEWORK", "low","ongoing", "01/01/23"));
        task_rvList.add(new task_rv("ITSYSOP HOMEWORK", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("LCFILIB HOMEWORK", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("GESPORTS HOMEWORK", "low","ongoing", "01/01/23"));
        task_rvList.add(new task_rv("1", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("2", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("3", "low","ongoing", "01/01/23"));
        task_rvList.add(new task_rv("1", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("2", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("3", "low","ongoing", "01/01/23"));
        task_rvList.add(new task_rv("1", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("2", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("3", "low","ongoing", "01/01/23"));
        task_rvList.add(new task_rv("1", "high","complete", "01/01/23"));
        task_rvList.add(new task_rv("2", "medium","incomplete", "01/01/23"));
        task_rvList.add(new task_rv("3", "low","ongoing", "01/01/23"));
        return task_rvList;

    }
    }