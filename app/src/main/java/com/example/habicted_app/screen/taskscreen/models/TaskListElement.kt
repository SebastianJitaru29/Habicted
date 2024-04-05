package com.example.habicted_app.screen.taskscreen.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TaskListElement(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
)
