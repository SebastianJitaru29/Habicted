package com.example.habicted_app.auth.data

import com.example.habicted_app.auth.Resource
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(email: String,password: String): Flow<Resource<AuthResult>>
}