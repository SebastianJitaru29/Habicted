package com.example.habicted_app.auth.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habicted_app.auth.Resource
import com.example.habicted_app.auth.data.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _authState = Channel<AuthState> {}
    val authState = _authState.receiveAsFlow()

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _authState.send(AuthState(isSuccess = "Sign up Success"))
                    loginUser(email, password)
                }

                is Resource.Loading -> {
                    _authState.send(AuthState(isLoading = true))
                }

                is Resource.Error -> {
                    _authState.send(AuthState(isError = result.message))
                }
            }
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _authState.send(AuthState(isSuccess = "Sign In Success"))
                }

                is Resource.Loading -> {
                    _authState.send(AuthState(isLoading = true))
                }

                is Resource.Error -> {
                    _authState.send(AuthState(isError = result.message))
                }
            }
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            repository.recoverPass(email).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.send(AuthState(isSuccess = "Recovery email has been sent"))
                    }

                    is Resource.Loading -> {
                        _authState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {

                        _authState.send(AuthState(isError = result.message))
                    }
                }
            }
        }
    }
}