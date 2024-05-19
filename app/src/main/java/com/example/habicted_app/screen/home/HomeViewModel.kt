package com.example.habicted_app.screen.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.remote.RemoteGroupRepository
import com.example.habicted_app.screen.preferences.MyPreferencesDataStore
import com.example.habicted_app.screen.preferences.NetworkPreference
import com.example.habicted_app.screen.taskscreen.TaskUIEvents
import com.example.habicted_app.screen.taskscreen.TaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val myPreferencesDataStore: MyPreferencesDataStore,
) : ViewModel() {

    private val _groupsList = MutableStateFlow<List<Group>>(emptyList())
    val groupsList = _groupsList.asStateFlow()

    private val _tasksList = MutableStateFlow<List<Task>>(emptyList())
    val tasksList = _tasksList.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var allTasks: Set<Task> = emptySet()

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    private suspend fun fetchData() {
        _groupsList.update { groupRepository.getUserGroups() }

        _groupsList.value.forEach { group ->
            allTasks = allTasks.plus(groupRepository.getGroupTasks(group.id))
        }

        fetchTasksByDate(_selectedDate.value)
    }


    fun onEvent(event: HomeUiEvents) {
        when (event) {
            is HomeUiEvents.SaveTask -> addTask(event.task)
            is HomeUiEvents.SaveGroup -> addGroup(event.group)
            is HomeUiEvents.SelectDate -> setDate(event.date)
            is HomeUiEvents.FilterTasksByDate -> fetchTasksByDate(event.date)
            is HomeUiEvents.UpdateNetworkCurrentStatus -> checkNetworkStatus(event.context)
        }
    }

    private fun setDate(date: LocalDate) {
        _selectedDate.update { date }
        Log.d("HomeViewModel", "Selected date: $date")
    }

    private fun addTask(newTask: Task) {
        viewModelScope.launch {
            val members = groupRepository.getGroup(newTask.groupId)?.members?.size
            newTask.total = members ?: 0
            groupRepository.addTaskToGroup(newTask, newTask.groupId)
            fetchData()
        }
    }

    private fun addGroup(group: Group) {
        viewModelScope.launch {
            groupRepository.insertGroup(group)
            //TODO: wait for confirmation and then update the UI
            _groupsList.update { groupRepository.getUserGroups() }
            Log.d("HomeViewModel", "New groups: ${_groupsList.value.map { it.name }}")
        }
    }

    private fun fetchTasksByDate(date: LocalDate) {
        val filteredTasks = allTasks.filter { it.date == date }
        Log.d("HomeViewModel", "Filtered tasks: ${filteredTasks.map { it }}")
        _tasksList.update { filteredTasks }
        _selectedDate.update { date }
    }

    fun onTaskEvent(event: TaskUIEvents): TaskUIState? {
        when (event) {
            is TaskUIEvents.ConvertTaskToTaskUIState -> {
                return getTaskUIStateWithGroupInfo(event.task)
            }

            is TaskUIEvents.UpdateIsDone -> updateTaskStatus(event.isDone, event.task)
        }
        return null
    }

    private fun getTaskUIStateWithGroupInfo(task: Task): TaskUIState {
        val group = _groupsList.value.first { it.id == task.groupId }
        return TaskUIState(
            task = task,
            groupName = group.name,
            color = Color(group.color),
        )
    }

    private fun updateTaskStatus(status: Boolean, task: Task) {
        // TODO: Update the task doneBy +1
        val updatedTask = task.copy(isDone = status)
        if (groupRepository is RemoteGroupRepository) {
            viewModelScope.launch {
                groupRepository.updateTasksStatus(updatedTask, onSuccess = {
                    val updatedTasks =
                        _tasksList.value.map { if (it.id == updatedTask.id) updatedTask else it }
                    _tasksList.value = updatedTasks
                    allTasks =
                        allTasks.map { if (it.id == updatedTask.id) updatedTask else it }.toSet()
                    fetchTasksByDate(_selectedDate.value)
                }, onFailure = {
                    Log.d("HomeViewModel", "Task update failed")
                })
            }
        }
    }

    private fun checkNetworkStatus(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        var priority: NetworkPreference? = null
        viewModelScope.launch {
            priority = myPreferencesDataStore.taskStatusFlow.first().netpreference
        }

        when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> {
                Toast.makeText(context, "Connected to wifi", Toast.LENGTH_SHORT).show()
            }

            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> {
                if (priority == NetworkPreference.WIFI)
                    Toast.makeText(context, "Please connect to a wifi network", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(context, "Connected to mobile data", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

enum class NetworkStatus {
    Wifi, Mobile, None
}
