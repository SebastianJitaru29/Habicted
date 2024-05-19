package com.example.habicted_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.habicted_app.screen.invitations.NotificationScreen
import com.example.habicted_app.screen.invitations.NotificationsViewModel

@Composable
fun NotificationsRoute(modifier: Modifier = Modifier, viewModel: NotificationsViewModel) {
    viewModel.fetchInvitations { }
    NotificationScreen(
        invitations = viewModel.invitationsList.collectAsState().value,
        onAccept = viewModel::acceptInvitation,
        onDecline = viewModel::declineInvitation,
        reload = viewModel::fetchInvitations,
    )
}