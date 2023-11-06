package com.mobdeve.s17.TaskBuddy.mco1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class task_adapter extends RecyclerView.Adapter<task_adapter.ViewHolder> {

    Context context;
    List<task_rv> task_rvList;

    public task_adapter(Context context, List<task_rv> task_rvList){
        this.context = context;
        this.task_rvList = task_rvList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(task_rvList != null && task_rvList.size() > 0){
            task_rv model = task_rvList.get(position);
            holder.name_rv.setText(model.getName());
            holder.priority_rv.setText(model.getPriority());
            holder.status_rv.setText(model.getStatus());
            holder.date_rv.setText(model.getDate());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final task_rv model = task_rvList.get(position);

                Intent intent = new Intent(context, taskDetailsActivity.class);
                intent.putExtra("task_name", model.getName());
                intent.putExtra("task_date", model.getDate());
                intent.putExtra("task_status", model.getStatus());
                intent.putExtra("task_priority", model.getPriority());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return task_rvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name_rv, priority_rv, date_rv, status_rv;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name_rv = itemView.findViewById(R.id.name_rv);
            priority_rv = itemView.findViewById(R.id.priority_rv);
            status_rv = itemView.findViewById(R.id.status_rv);
            date_rv = itemView.findViewById(R.id.date_rv);
        }
    }
}
