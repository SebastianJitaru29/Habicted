package com.example.habicted_app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.TaskRepository
import com.example.habicted_app.data.repository.UserRepository
import com.example.habicted_app.data.repository.local.LocalGroupRepository
import com.example.habicted_app.data.repository.local.LocalTaskRepository
import com.example.habicted_app.data.repository.local.LocalUserRepository
import com.example.habicted_app.data.repository.remote.RemoteGroupRepository
import com.example.habicted_app.data.repository.remote.RemoteTaskRepository
import com.example.habicted_app.data.repository.remote.RemoteUserRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HabictedApp : Application() {
    lateinit var container: HabictedAppContainer

    //User hilt or dagger to inject userRepository
    override fun onCreate() {
        super.onCreate()
        container = HabictedAppContainer(applicationContext)
    }
}


//https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3
class HabictedAppContainer(private val context: Context) {
    private val localGroupRepository = LocalGroupRepository()
    private val remoteGroupRepository = RemoteGroupRepository()

    private val localTaskRepository = LocalTaskRepository()
    private val remoteTaskRepository = RemoteTaskRepository()

    private val localUserRepository = LocalUserRepository()
    private val remoteUserRepository = RemoteUserRepository()

    val groupRepository: GroupRepository
        get() = localGroupRepository //if (isNetworkAvailable(context)) remoteGroupRepository else localGroupRepository

    val taskRepository: TaskRepository
        get() = if (isNetworkAvailable(context)) remoteTaskRepository else localTaskRepository

    val userRepository: UserRepository
        get() = localUserRepository//if (isNetworkAvailable(context)) remoteUserRepository else localUserRepository

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}