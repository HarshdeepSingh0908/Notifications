package com.harsh.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.harsh.notifications.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "my_channel_id"
    private val CHANNEL_NAME = "My Channel"
    private val CHANNEL_DESCRIPTION = "My Notification Channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.btnNoCancel.setOnClickListener {
            showNoCancelNotification()
        }

        binding.btnLargeTextNotofication.setOnClickListener {

            sendNotificationWithLargeText(this,"This is Title","This is Message",R.drawable.send_message,"This is LARGE text")
        }

        binding.btnAutoCancelAfterSometime.setOnClickListener {
            showAutoCancelNotification()
        }

        binding.btnImageSmall.setOnClickListener {
            sendNotificationWithSmallImage(this,"Title","Message",R.drawable.send_message)
        }

        binding.btnImageExpanded.setOnClickListener {
            sendNotificationWithExpandableImage(this,"This is Title","This is Message",R.drawable.send_message)

        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun showNotification(builder: NotificationCompat.Builder) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun showNoCancelNotification() {
        val builder = buildNotification()
        showNotification(builder)
    }



    private fun showAutoCancelNotification() {
        val builder = buildNotification().setTimeoutAfter(5000) // Auto cancel after 5 seconds
        showNotification(builder)
    }

    private fun buildNotification(): NotificationCompat.Builder {

        val intent = Intent(this, AlertDetails::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.send_message)
            .setContentTitle("This is Text")
            .setContentText("This is body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
    fun sendNotificationWithExpandableImage(context: Context, title: String, message: String, imageResId: Int) {
        val intent = Intent(context, AlertDetails::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, imageResId))
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(context.resources, imageResId))) // Set the big picture using the same resource
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    fun sendNotificationWithSmallImage(context: Context, title: String, message: String, smallImageResId: Int) {
        // Create an Intent for the notification to open when tapped
        val intent = Intent(context, AlertDetails::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Create a notification builder with small icon only
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallImageResId)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun sendNotificationWithLargeText(context: Context, title: String, message: String, smallImageResId: Int, largeText: String) {
        val intent = Intent(context, AlertDetails::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallImageResId)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(largeText))


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


}
