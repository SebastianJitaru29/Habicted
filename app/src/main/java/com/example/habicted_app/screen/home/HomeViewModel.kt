package com.example.habicted_app.screen.home

import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.TaskRepository
import com.example.habicted_app.screen.groups.GroupUIState
import com.example.habicted_app.screen.taskscreen.TaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val groupRepository: GroupRepository,
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUIState())
    val homeUiState = _homeUiState.asStateFlow()

    init {
        loadTasks()
        loadGroups()
    }

    private fun loadGroups() {
        groupRepository.getAllGroups().let { groups ->
            _homeUiState.update { state ->
                state.copy(
                    groups = groups.map { GroupUIState(it) }
                )
            }
        }
    }

    private fun loadTasks() {
        tasksRepository.getTaskByDate(LocalDate.now()).let { tasks ->
            _homeUiState.update { state ->
                state.copy(
                    tasks = tasks.map { TaskUIState(it) }
                )
            }
        }
    }

    fun onEvent(event: HomeUiEvents) {
        when (event) {
            is HomeUiEvents.SaveTask -> addTask(event.task)
            is HomeUiEvents.SaveGroup -> addGroup(event.group)
            is HomeUiEvents.FilterTasksByDate -> filterTasksByDate(event.date)
        }
    }

    private fun addTask(newTask: Task) {
        tasksRepository.insertTask(newTask)
        _homeUiState.update {
            it.copy(
                tasks = it.tasks + TaskUIState(newTask)
            )
        }
    }

    private fun addGroup(group: Group) {
        groupRepository.insertGroup(group)
        _homeUiState.update {
            it.copy(
                groups = it.groups + GroupUIState(group)
            )
        }
    }

    private fun filterTasksByDate(date: LocalDate) {
        val tasks = tasksRepository.getTaskByDate(date)
        _homeUiState.update { state ->
            state.copy(
                tasks = tasks.map { TaskUIState(it) }
            )
        }
    }
}