package com.example.habicted_app.screen.taskscreen.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Data class to hold real calendar data

data class CalendarUiModel(
    val selectedDate: Date,  //Date selected by the user
    val visibleDates: List<Date> //Dates shown in the UI
){
    val startDate: Date = visibleDates.first()
    val endDate: Date = visibleDates.last()

    data class Date(
        val date: LocalDate,
        val isSelected:Boolean,
        val isToday:Boolean
    ){
        @RequiresApi(Build.VERSION_CODES.O)
        val day: String = date.format(DateTimeFormatter.ofPattern("E"))
    }
}