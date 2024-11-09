package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseHelper databaseHelper;
    private TextView noTasksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        noTasksTextView = findViewById(R.id.noTasksTextView);
        databaseHelper = new DatabaseHelper(this);

        taskList = new ArrayList<>();
        loadTasksFromDatabase();

        taskAdapter = new TaskAdapter(this, taskList, task -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            intent.putExtra("taskId", task.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        fabAddTask.setOnClickListener(v -> showAddTaskPopup());
    }


    private void loadTasksFromDatabase() {
        taskList.clear();
        Cursor cursor = databaseHelper.getAllTask();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String details = cursor.getString(cursor.getColumnIndexOrThrow("details"));
                String lastEdited = cursor.getString(cursor.getColumnIndexOrThrow("last_edited"));
                String priority = cursor.getString(cursor.getColumnIndexOrThrow("priority"));
                Log.d("TaskPriority", "Task: " + title + ", Priority: " + priority); // Log statement

                taskList.add(new Task(id, title, details, lastEdited, priority));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (taskList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noTasksTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noTasksTextView.setVisibility(View.GONE);
        }

        // Notify the adapter if the list is updated
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }


    private void showAddTaskPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_task, null);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();

        EditText etTaskTitle = popupView.findViewById(R.id.etTaskTitle);
        EditText etTaskDescription = popupView.findViewById(R.id.etTaskDescription);
        Button btnSaveTask = popupView.findViewById(R.id.btnSaveTask);
        Spinner prioritySpinner = popupView.findViewById(R.id.spinner_priority);  // Initialize from popupView

        // Set up the spinner with priority levels
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        btnSaveTask.setOnClickListener(view -> {
            String title = etTaskTitle.getText().toString().trim();
            String details = etTaskDescription.getText().toString().trim();
            String selectedPriority = prioritySpinner.getSelectedItem().toString().trim();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (!title.isEmpty()) {
                long result = databaseHelper.addTask(title, details, currentDate, selectedPriority);
                if (result != -1) {
                    Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
                    loadTasksFromDatabase(); // Refresh task list
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromDatabase();
    }
}
