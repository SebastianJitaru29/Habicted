package com.example.habicted_app.screen.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.TaskRepository
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
    private val tasksRepository: TaskRepository,
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
            groupRepository.addTaskToGroup(newTask, newTask.groupId)
            fetchData()
        }
    }

    private fun addGroup(group: Group) {
        viewModelScope.launch {
            groupRepository.insertGroup(group)
            //TODO: wait for confirmation and then update the UI
            _groupsList.update { groupRepository.getAllGroups() }
            Log.d("HomeViewModel", "New groups: ${_groupsList.value.map { it.name }}")
        }
    }

    private fun fetchTasksByDate(date: LocalDate) {
        val filteredTasks = allTasks.filter { it.date == date }
        Log.d("HomeViewModel", "Filtered tasks: ${filteredTasks.map { it.name }}")
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
//        val group: Group =
//            groupRepository.getGroup(task.groupId) ?: throw Exception("Group not found")
//        return TaskUIState(
//            task = task,
//            groupName = group.name,
//            color = Color(group.color),
//        )
        return TaskUIState(task)
    }

    private fun updateTaskStatus(status: Boolean, task: Task) {
        val newTask = task.copy(isDone = status)
        viewModelScope.launch {
            tasksRepository.updateTask(newTask)

            val index = _tasksList.value.indexOfFirst { it.id == task.id }
            if (index != -1) {
                val updatedTasks = _tasksList.value.toMutableList()
                updatedTasks[index] = newTask
                _tasksList.update { updatedTasks }
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
