package com.arya.e_kinerja.utils

import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(source: String?, oldPattern: String, newPattern: String?): String {
    var simpleDateFormat = SimpleDateFormat(oldPattern, Locale.getDefault())
    val date = if (source != null) {
        simpleDateFormat.parse(source) as Date
    } else {
        Calendar.getInstance().time
    }
    if (newPattern != null) {
        simpleDateFormat = SimpleDateFormat(newPattern, Locale.getDefault())
    }
    return simpleDateFormat.format(date)
}

fun dateTimeFormat(date: String, start: String?, end: String?, duration: String): String {
    return if (start != null && end != null) {
        "${dateFormat(date, "yyyy-MM-dd", "dd-MM-yyyy")} • ${dateFormat(start, "HH:mm:ss", "HH:mm")} - ${dateFormat(end, "HH:mm:ss", "HH:mm")} ( $duration Menit )"
    } else {
        "${dateFormat(date, "yyyy-MM-dd", "dd-MM-yyyy")} • $duration Menit"
    }
}