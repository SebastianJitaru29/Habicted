package com.example.habicted_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habicted_app.screen.invitations.NotificationScreen
import com.example.habicted_app.screen.invitations.NotificationsViewModel

@Composable
fun NotificationsRoute(modifier: Modifier = Modifier) {
    val viewModel: NotificationsViewModel = hiltViewModel()
    viewModel.fetchInvitations()
    NotificationScreen(
        invitations = viewModel.invitationsList.value,
        onAccept = viewModel::acceptInvitation,
        onDecline = viewModel::declineInvitation
    )
}