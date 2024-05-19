package com.example.habicted_app.screen.invitations

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Invitation
import com.example.habicted_app.data.model.InvitationStatus
import com.example.habicted_app.data.repository.remote.InvitationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        invitationRepository.acceptInvitation(invitation) {
            //On accepted
            _invitationsList.update {
                _invitationsList.value.toMutableList().apply {
                    find { it.id == invitation.id }?.status = InvitationStatus.ACCEPTED
                }
            }
        }
    }

    fun declineInvitation(invitation: Invitation) {
        invitationRepository.declineInvitation(invitation) {
            //On declined
            _invitationsList.update {
                _invitationsList.value.map {
                    if (it.id == invitation.id) {
                        it.copy(status = InvitationStatus.DECLINED)
                    } else {
                        it
                    }

                }
            }
        }
    }

    fun inviteUser(group: Group, id: String) {
        invitationRepository.inviteUser(group = group, toUserId = id)
    }
}