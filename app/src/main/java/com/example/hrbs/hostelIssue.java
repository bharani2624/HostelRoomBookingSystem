package com.example.hrbs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class hostelIssue extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("Issues").child("hostelIssues");
    private DatabaseReference issuesReference = database.getReference("Issues"); // Reference to the Issues node for listening
    OverAllIssues overAllIssues;
    TextInputEditText name, rollno, issues;
    Spinner spinnerIssues;
    Button book;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_issue);

        // Set up edge-to-edge support for modern UI layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        book = findViewById(R.id.bookIssue);
        spinnerIssues = findViewById(R.id.roomIssueSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hostelissues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssues.setAdapter(adapter);

        // Button click listener to submit an issue
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = findViewById(R.id.nameIssueCreator);
                rollno = findViewById(R.id.rollNoIssueCreator);
                issues = findViewById(R.id.issueDescription);

                String nameOfIssueCreator = name.getText().toString();
                String rollNoOfIssueCreator = rollno.getText().toString();
                String type = spinnerIssues.getSelectedItem().toString();
                String issuebyCreator = issues.getText().toString();

                overAllIssues = new OverAllIssues(nameOfIssueCreator, rollNoOfIssueCreator, type, issuebyCreator, false);

                reference.child(rollNoOfIssueCreator).setValue(overAllIssues)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(hostelIssue.this, "Issue Submitted Successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(hostelIssue.this, "Failed to Submit Issue", Toast.LENGTH_SHORT).show());
            }
        });

        // Subscribe to "issues" topic to receive notifications
        FirebaseMessaging.getInstance().subscribeToTopic("issues")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Subscribed to issue notifications", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up listener for status changes in the Issues node
        issuesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot issueSnapshot : categorySnapshot.getChildren()) {
                        Boolean status = issueSnapshot.child("status").getValue(Boolean.class);
                        String issueType = issueSnapshot.child("type").getValue(String.class);
                        String rollNo = issueSnapshot.child("rollNo").getValue(String.class);

                        // If the issue is marked as resolved, send a notification
                        if (status != null && status) {
                            sendNotification(issueType, rollNo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(hostelIssue.this, "Failed to listen for changes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to trigger a notification when an issue is resolved
    private void sendNotification(String issueType, String rollNo) {
        String channelId = "issue_resolved_notifications";
        String channelName = "Issue Resolved Notifications";

        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Make sure this icon exists in your drawable folder
                .setContentTitle("Issue Resolved")
                .setContentText("Issue Type: " + issueType + " for Roll No: " + rollNo + " has been resolved.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Check for notification permission and display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }
}
