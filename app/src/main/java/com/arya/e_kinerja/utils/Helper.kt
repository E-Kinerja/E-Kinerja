package com.arya.e_kinerja.utils

import android.content.Context
import com.arya.e_kinerja.R
import java.util.*

fun getMaximumDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun getDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getNameOfTheMonth(context: Context, day: Int?): String {
    val calendar = Calendar.getInstance()
    val arrayBulan = context.resources.getStringArray(R.array.bulan)
    return if (day != null) {
        arrayBulan[day]
    } else {
        arrayBulan[calendar.get(Calendar.MONTH)]
    }
}