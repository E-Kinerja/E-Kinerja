package com.arya.e_kinerja.utils

import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(source: String?, pattern: String): String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val date = if (source != null) {
        simpleDateFormat.parse(source) as Date
    } else {
        Calendar.getInstance().time
    }
    return simpleDateFormat.format(date)
}

fun dateTimeFormat(date: String, start: String?, end: String?, duration: String): String {
    return if (start != null && end != null) {
        "${dateFormat(date, "dd-MM-yyyy")} • ${dateFormat(start, "HH:mm")} - ${dateFormat(end, "HH:mm")} ( $duration Menit )"
    } else {
        "${dateFormat(date, "dd-MM-yyyy")} • $duration Menit"
    }
}