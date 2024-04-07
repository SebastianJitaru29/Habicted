package com.example.habicted_app.screen.taskscreen.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TaskListElement(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    var isChecked:Boolean
)
