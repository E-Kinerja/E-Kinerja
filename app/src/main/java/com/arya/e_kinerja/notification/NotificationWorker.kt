package com.arya.e_kinerja.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.arya.e_kinerja.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorker : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifId = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING

        showToast(context, title, message)

        if (message != null) {
            showAlarmNotification(context, title, message, notifId)
        }
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setOneTimeAlarm(context: Context, type: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationWorker::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val calendar = Calendar.getInstance()
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val minuteFormat = SimpleDateFormat("mm", Locale.getDefault())

        val year = yearFormat.format(calendar.time).toInt()
        val month = monthFormat.format(calendar.time).toInt()
        val date = dateFormat.format(calendar.time).toInt()
        val hour = hourFormat.format(calendar.time).toInt()
        val minute = minuteFormat.format(calendar.time).toInt()
        val second = 0

        calendar.set(year, month - 1, date, hour, minute, second)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(
            context,
            "Alarm is set to $year-$month-$date $hour:${minute}:$second",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val channelId = "channel 1"
        val channelName = "notification worker"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_fab_print)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    companion object {
        const val TYPE_ONE_TIME = "E-kinerja"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        const val MESSAGE_ONE_TIME = "!HARI TERAKHIR PERIODE PENGISIAN! Dimohon untuk segera mengisi Tugas Aktivitas!"

        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101
    }

//    //TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
//    @SuppressLint("InlinedApi")
//
//
//    @SuppressLint("InlinedApi")
//    fun cancelAlarm(context: Context) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, NotificationWorker::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        pendingIntent.cancel()
//        alarmManager.cancel(pendingIntent)
//
//        Toast.makeText(context, "Alarm has been canceled", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun showNotification(context: Context) {
//        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
////        val intent = Intent(context, HomeActivity::class.java)
////        val pendingIntent : PendingIntent? = TaskStackBuilder.create(context).run {
////            addNextIntentWithParentStack(intent)
////            getPendingIntent(NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)
////        }
//
//        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationStyle = NotificationCompat.InboxStyle()
////        val timeString = context.resources.getString(R.string.notification_message_format)
////        content.forEach {
////            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
////            notificationStyle.addLine(courseData)
////        }
//
//        val builder = NotificationCompat.Builder(context, "notif")
//            .setSmallIcon(R.drawable.ic_fab_input)
//            .setContentTitle("ALERTA ALERTA")
//            .setStyle(notificationStyle)
//            .setContentText("Isi woy")
//            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
//            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
////
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "notif",
//                "E-kinerja",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            channel.enableVibration(true)
//            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
//
//            builder.setChannelId("notif")
//            notificationManagerCompat.createNotificationChannel(channel)
//        }
//
//        val notification = builder.build()
//        notificationManagerCompat.notify(32, notification)
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun getDay(): String {
//        val dateFormat = SimpleDateFormat("EEEE")
//        return dateFormat.format(Date())
//    }
}