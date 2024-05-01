package com.example.habicted_app.auth.model.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habicted_app.auth.Resource
import com.example.habicted_app.auth.data.AuthRepository
import com.example.habicted_app.auth.model.forgotpas.ForgotPassState
import com.example.habicted_app.auth.model.signin.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _forgotPassState = Channel<ForgotPassState>()
    val forgotPassState = _forgotPassState.receiveAsFlow()

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            repository.recoverPass(email).collect { result ->
                when(result){
                    is Resource.Success -> {
                        _forgotPassState.send(ForgotPassState(isSuccess = "Recovery email has been sent"))
                    }
                    is Resource.Loading -> {
                        _forgotPassState.send(ForgotPassState(isLoading = true))
                    }
                    is Resource.Error -> {

                        _forgotPassState.send(ForgotPassState(isError = result.message))
                    }
                }
            }
        }
    }
}
