package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity {

    private EditText etTaskDetails;
    private TextView tvTaskTitle, tvLastEdited;
    private DatabaseHelper databaseHelper;
    private Task task;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        etTaskDetails = findViewById(R.id.etTaskDetails);
        tvTaskTitle = findViewById(R.id.taskDetailsTitle);
        tvLastEdited = findViewById(R.id.tvLastEdited);
        ImageView btnBack = findViewById(R.id.btnBack);
        FloatingActionButton fabDeleteTask = findViewById(R.id.fabDeleteTask);

        databaseHelper = new DatabaseHelper(this);
        int taskId = getIntent().getIntExtra("taskId", -1);

        // Load task details
        task = databaseHelper.getTaskById(taskId);
        if (task != null) {
            tvTaskTitle.setText(task.getTitle());
            etTaskDetails.setText(task.getDetails());
            tvLastEdited.setText("Last Edited: " + task.getLastEdited());
        }

        // Back button logic to save updates
        btnBack.setOnClickListener(v -> {
            if (task != null) {
                updateTaskDetails();
                finish();
            }
        });

        // Delete button logic
        fabDeleteTask.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.danger_triangle_svgrepo_com)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.deleteTask(task.getId());
                        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public void onBackPressed() {
        if (task != null) {
            updateTaskDetails();
        }
        super.onBackPressed();
    }

    private void updateTaskDetails() {
        String updatedDetails = etTaskDetails.getText().toString().trim();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        databaseHelper.updateTask(task.getId(), task.getTitle(), updatedDetails, currentDate);
        Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
    }
}
