package com.arya.e_kinerja.utils

import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

fun openMaterialTimePicker(
    fragmentManager: FragmentManager,
    textInputEditText: TextInputEditText,
    currentTime: String
): MaterialTimePicker {
    val calendar = Calendar.getInstance()

    val hour = if (currentTime != "") {
        currentTime.split(":")[0].toInt()
    } else {
        calendar.get(Calendar.HOUR_OF_DAY)
    }

    val minute = if (currentTime != "") {
        currentTime.split(":")[1].toInt()
    } else {
        calendar.get(Calendar.MINUTE)
    }

    val timePicker =
        MaterialTimePicker.Builder()
            .setTitleText("Pilih Waktu")
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .build()

    timePicker.show(fragmentManager, "timePicker")

    timePicker.addOnPositiveButtonClickListener {
        val hourOfDay = timePicker.hour
        val minuteOfHour = timePicker.minute

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minuteOfHour)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        textInputEditText.setText(dateFormat.format(calendar.time))
    }

    return timePicker
}