package com.example.hrbs;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "issue_resolved_notifications";
    private static final String CHANNEL_NAME = "Issue Resolved Notifications";
    private static final String PREFS_NAME = "FCM_PREFS";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Check if the message contains data
        if (remoteMessage.getData().size() > 0) {
            String messageId = remoteMessage.getMessageId();

            // Avoid processing duplicate messages
            if (isMessageAlreadyProcessed(messageId)) {
                return; // Skip if already handled
            }

            String issueType = remoteMessage.getData().get("issueType");
            String rollNo = remoteMessage.getData().get("rollNo");

            // Save messageId to prevent duplicate processing
            saveMessageId(messageId);

            // Send the notification
            sendNotification(issueType, rollNo);
        }
    }

    private void sendNotification(String issueType, String rollNo) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your icon
                .setContentTitle("Issue Resolved")
                .setContentText("Issue Type: " + issueType + " for Roll No: " + rollNo + " has been resolved.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Use a consistent notification ID to avoid duplicates (use issueType hash for uniqueness)
        int notificationId = issueType != null ? issueType.hashCode() : (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

    private boolean isMessageAlreadyProcessed(String messageId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.contains(messageId);
    }

    private void saveMessageId(String messageId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(messageId, true).apply();
    }
}
