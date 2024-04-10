package com.example.habicted_app

import android.app.Application
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.TaskRepository
import com.example.habicted_app.data.repository.UserRepository
import com.example.habicted_app.data.repository.local.LocalGroupRepository
import com.example.habicted_app.data.repository.local.LocalTaskRepository
import com.example.habicted_app.data.repository.local.LocalUserRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HabictedApp : Application() {
    lateinit var container: HabictedAppContainer

    //User hilt or dagger to inject userRepository
    override fun onCreate() {
        super.onCreate()
        container = HabictedAppContainer()
    }
}


//https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3
class HabictedAppContainer {
    val userRepository: UserRepository = LocalUserRepository()
    val groupRepository: GroupRepository = LocalGroupRepository()
    val taskRepository: TaskRepository = LocalTaskRepository()
}