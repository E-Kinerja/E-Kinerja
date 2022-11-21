
package com.arya.e_kinerja.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavDeepLinkBuilder
import com.arya.e_kinerja.R
import com.arya.e_kinerja.ui.main.MainActivity
import com.arya.e_kinerja.utils.getDayOfMonth
import com.arya.e_kinerja.utils.getMaximumDayOfMonth
import java.io.File
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val title = "Hari terakhir periode pengisian"
        val message = "Dimohon untuk segera mengisi tugas aktivitas"

        when (getDayOfMonth()) {
            7, 15, 22, getMaximumDayOfMonth() -> {
                showAlarmNotification(context, title, message, ID_REMINDER, null)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setReminderAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REMINDER,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun cancelReminderAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REMINDER,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int,
        fileName: String?
    ) {
        val channelId = "channel 1"
        val channelName = "Reminder Notification"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = if (fileName != null) {
            pendingIntentToOpenFile(context, fileName)
        } else {
            pendingIntentToOpenApp(context)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_logo_kab_sidoarjo)
            .setSound(alarmSound)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun pendingIntentToOpenFile(context: Context, fileName: String): PendingIntent {
        val filePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()

        val file = File(filePath, fileName)

        val apkUri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.setDataAndType(apkUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val intentChooser = Intent.createChooser(intent, "Buka dengan")

        return PendingIntent.getActivity(
            context,
            0,
            intentChooser,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
    }

    private fun pendingIntentToOpenApp(context: Context): PendingIntent {
        val args = Bundle()
        args.putString("notifikasi", "notifikasi")

        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.splashFragment)
            .setArguments(args)
            .createPendingIntent()
    }

    companion object {
        const val ID_REMINDER = 100
        const val ID_DOWNLOAD = 101
    }
}