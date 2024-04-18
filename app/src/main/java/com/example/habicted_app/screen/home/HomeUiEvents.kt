package com.example.habicted_app.screen.home

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import java.time.LocalDate

sealed class HomeUiEvents {
    data class SaveTask(val task: Task) : HomeUiEvents()
    data class SaveGroup(val group: Group) : HomeUiEvents()
    data class FilterTasksByDate(val date: LocalDate) : HomeUiEvents()
    data class NetworkCurrentStatus(val status: NetworkStatus) : HomeUiEvents()
}
