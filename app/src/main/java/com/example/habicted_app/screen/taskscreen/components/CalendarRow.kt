package com.example.habicted_app.screen.taskscreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.screen.home.HomeUiEvents
import com.example.habicted_app.screen.taskscreen.calendar.CalendarData
import com.example.habicted_app.screen.taskscreen.calendar.CalendarDataSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarRow(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onEvent: (HomeUiEvents) -> Unit,
) {
    val dataSource = CalendarDataSource()
    // we use `mutableStateOf` and `remember` inside composable function to schedules recomposition
    var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = selectedDate)) }

    CalendarHeader(
        data = calendarUiModel,
        onPrevClickListener = { startDate ->
            // refresh the CalendarUiModel with new data
            // by get data with new Start Date (which is the startDate-1 from the visibleDates)
            val finalStartDate = startDate.minusDays(1)
            calendarUiModel = dataSource.getData(
                startDate = finalStartDate,
                lastSelectedDate = calendarUiModel.selectedDate.date
            )
        },
        onNextClickListener = { endDate ->
            // refresh the CalendarUiModel with new data
            // by get data with new Start Date (which is the endDate+2 from the visibleDates)
            val finalStartDate = endDate.plusDays(2)
            calendarUiModel = dataSource.getData(
                startDate = finalStartDate,
                lastSelectedDate = calendarUiModel.selectedDate.date
            )
        }
    )
    CalendarContent(
        data = calendarUiModel,
        onDateClickListener = { date ->
            // refresh the CalendarUiModel with new data
            // by changing only the `selectedDate` with the date selected by User
            calendarUiModel = calendarUiModel.copy(
                selectedDate = date,
                visibleDates = calendarUiModel.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
            // Update TaskList and selected date
            onEvent(HomeUiEvents.SelectDate(date.date))
            onEvent(HomeUiEvents.FilterTasksByDate(date.date))
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    data: CalendarData,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
    Row {
        Text(
            text = data.selectedDate.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = { onPrevClickListener(data.startDate.date) }) {
            Icon(

                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Previous"
            )
        }
        IconButton(onClick = { onNextClickListener(data.endDate.date) }) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarContent(
    data: CalendarData,
    onDateClickListener: (CalendarData.Date) -> Unit,
) {
    LazyRow {
        items(items = data.visibleDates) { date ->
            CalendarItem(
                date = date,
                onClickListener = onDateClickListener
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarItem(date: CalendarData.Date, onClickListener: (CalendarData.Date) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { onClickListener(date) },
        colors = CardDefaults.cardColors(
            // background colors of the selected date
            // and the non-selected date are different
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.inversePrimary
            } else {
                MaterialTheme.colorScheme.primary
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DayCardPrev() {
    Row {
        CalendarItem(date = CalendarData.Date(LocalDate.now(), true, false), { })
        CalendarItem(date = CalendarData.Date(LocalDate.now().plusDays(1), false, false), { })
    }
}
