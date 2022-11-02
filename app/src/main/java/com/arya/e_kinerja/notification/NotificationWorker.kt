package com.arya.e_kinerja.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.arya.e_kinerja.R
import com.arya.e_kinerja.utils.getDayOfMonth
import com.arya.e_kinerja.utils.getMaximumDayOfMonth
import java.util.*

class NotificationWorker : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifId = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING

        when(getDayOfMonth()) {
            7, 15, 22, getMaximumDayOfMonth() -> {
                showAlarmNotification(context, title, message.toString(), notifId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setRepeatingAlarm(context: Context, type: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationWorker::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, PendingIntent.FLAG_IMMUTABLE)
//        val pendingIntent = NavDeepLinkBuilder(context)
//            .setComponentName(MainActivity::class.java)
//            .setGraph(R.navigation.nav_graph)
//            .setDestination(R.id.tugasAktivitasFragment)
//            .createPendingIntent()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HALF_DAY,
            pendingIntent
        )
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val channelId = "channel 1"
        val channelName = "Reminder Notification"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_logo_kab_sidoarjo)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    companion object {
        const val TYPE_ONE_TIME = "OneTime"
        const val TYPE_REPEATING = "HARI TERAKHIR PENGISIAN AKTIVITAS"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        const val MESSAGE_REPEATING = "Dimohon untuk segera mengisi Tugas Aktivitas!"

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