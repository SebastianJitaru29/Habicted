package com.example.habicted_app.screen.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.TaskRepository
import com.example.habicted_app.screen.groups.GroupUIState
import com.example.habicted_app.screen.preferences.MyPreferencesDataStore
import com.example.habicted_app.screen.preferences.NetworkPreference
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
            is HomeUiEvents.UpdateNetworkCurrentStatus -> checkNetworkStatus(event.context)
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
