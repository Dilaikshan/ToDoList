package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private Spinner spinnerPriority;
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

        // Set up Spinner for priority levels
        spinnerPriority = findViewById(R.id.spinnerPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        // Set spinner selection based on task priority
        if (task != null) {
            String priority = task.getPriority();
            int position = adapter.getPosition(priority);
            spinnerPriority.setSelection(position);
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
        // Get the selected priority from the spinner
        String selectedPriority = spinnerPriority.getSelectedItem().toString();
        databaseHelper.updateTask(task.getId(), task.getTitle(), updatedDetails, currentDate, selectedPriority);
        Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
    }
}
