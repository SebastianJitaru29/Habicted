package com.example.habicted_app.screen.invitations

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.model.Invitation
import com.example.habicted_app.data.repository.remote.InvitationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel()
class NotificationsViewModel @Inject constructor(
    private val invitationRepository: InvitationRepository,
) : ViewModel() {

    private val _invitationsList = MutableStateFlow<List<Invitation>>(emptyList())
    val invitationsList = _invitationsList.asStateFlow()

    fun fetchInvitations(onFinish: () -> Unit) {
        invitationRepository.fetchInvitations { invitations ->
            _invitationsList.value = invitations
            Log.d("NotificationsViewModel", "Invitations fetched")
            onFinish()
        }
    }

    fun acceptInvitation(invitation: Invitation) {
        invitationRepository.acceptInvitation(invitation)
    }

    fun declineInvitation(invitation: Invitation) {
        invitationRepository.declineInvitation(invitation)
    }
}