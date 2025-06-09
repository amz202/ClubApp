package com.example.clubapp.push_notification

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.clubapp.R

/*
 * Copyright 2025 Abdul Majid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract notification title and body
        val title = remoteMessage.notification?.title ?: "Event Update"
        val body = remoteMessage.notification?.body ?: ""

        // Get the eventId from the data payload
        val eventId = remoteMessage.data["eventId"] ?: ""

        // Show the notification
        showNotification(title, body, eventId)
    }

    private fun showNotification(title: String, body: String, eventId: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_updates_channel",
                "Event Updates",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "event_updates_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        // Show the notification
        // Using eventId hashCode as notification ID to avoid overwriting notifications
        // from the same event but still allow multiple notifications from different events
        notificationManager.notify(eventId.hashCode(), notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}