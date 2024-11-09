package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final Context context;
    private final List<Task> taskList;
    private final OnTaskClickListener onTaskClickListener;

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener onTaskClickListener) {
        this.context = context;
        this.taskList = taskList;
        this.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvLastEdited.setText("Last edited: " + task.getLastEdited());
        holder.itemView.setOnClickListener(v -> onTaskClickListener.onTaskClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }



    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLastEdited;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvLastEdited = itemView.findViewById(R.id.tvLastEdited);
        }
    }
}
