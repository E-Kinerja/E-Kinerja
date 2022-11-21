package com.arya.e_kinerja.views

import androidx.fragment.app.FragmentManager
import com.arya.e_kinerja.utils.getMaximumDayOfMonth
import com.google.android.material.datepicker.*
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

fun openMaterialDatePicker(
    fragmentManager: FragmentManager,
    textInputEditText: TextInputEditText,
    currentDate: String
): MaterialDatePicker<*> {
    val calendar = Calendar.getInstance()

    if (currentDate != "") {
        val (year, month, date) = currentDate.split("-")
        calendar.set(year.toInt(), month.toInt() - 1, date.toInt())
    }

    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(calendar.timeInMillis)
            .setCalendarConstraints(getCalendarConstraints(setMinMaxDate(calendar)))
            .build()

    datePicker.show(fragmentManager, "datePicker")

    datePicker.addOnPositiveButtonClickListener {
        val formatter =
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(it))
        textInputEditText.setText(formatter)
    }

    return datePicker
}

private fun setMinMaxDate(calendar: Calendar): Pair<Calendar, Calendar> {
    val minDate = calendar.clone() as Calendar
    val maxDate = calendar.clone() as Calendar

    val date = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    when (date) {
        in 1..7 -> {
            minDate.set(year, month, 0)
            maxDate.set(year, month, 8)
        }
        in 8..15 -> {
            minDate.set(year, month, 7)
            maxDate.set(year, month, 16)
        }
        in 16..22 -> {
            minDate.set(year, month, 15)
            maxDate.set(year, month, 23)
        }
        in 23..getMaximumDayOfMonth() -> {
            minDate.set(year, month, 22)
            maxDate.set(year, month + 1, 1)
        }
    }

    return Pair(minDate, maxDate)
}

private fun getCalendarConstraints(
    minMaxDate: Pair<Calendar, Calendar>
): CalendarConstraints {
    val minDate = minMaxDate.first
    val maxDate = minMaxDate.second

    val dateValidatorMin: CalendarConstraints.DateValidator =
        DateValidatorPointForward.from(minDate.timeInMillis)
    val dateValidatorMax: CalendarConstraints.DateValidator =
        DateValidatorPointBackward.before(maxDate.timeInMillis)

    val listValidators = ArrayList<CalendarConstraints.DateValidator>()
    listValidators.apply {
        add(dateValidatorMin)
        add(dateValidatorMax)
    }

    val validators = CompositeDateValidator.allOf(listValidators)

    return CalendarConstraints.Builder()
        .setValidator(validators)
        .build()
}