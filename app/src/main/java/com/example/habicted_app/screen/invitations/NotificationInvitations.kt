package com.example.habicted_app.screen.invitations

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.data.model.Invitation
import com.example.habicted_app.data.model.InvitationStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    reload: (() -> Unit) -> Unit,
    invitations: List<Invitation>,
    onAccept: (Invitation) -> Unit,
    onDecline: (Invitation) -> Unit,
) {
    Scaffold(topBar = {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Group Invitations",
            style = MaterialTheme.typography.titleLarge,
        )
    }) {
        val state = rememberPullToRefreshState()
        if (state.isRefreshing) {
            LaunchedEffect(true) {
                reload { state.endRefresh() }
                Log.d("NotificationScreen", "Refreshing")
            }
        }
        Box(
            Modifier
                .nestedScroll(state.nestedScrollConnection)
                .padding(it)
        ) {
            LazyColumn {
                if (invitations.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            text = "No invitations",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    items(invitations) { invitation ->
                        NotificationCard(
                            invitation = invitation,
                            onAccept = { onAccept(invitation) },
                            onDecline = { onDecline(invitation) },
                        )
                    }
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
            )
        }
    }
}

@Composable
fun NotificationCard(
    invitation: Invitation,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column {

            // Display invitation information
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Invitation from ${invitation.fromUserName} to join group ${invitation.groupName}"
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                AssistChip(onClick = onAccept, label = {
                    Text(text = "Accept")
                }, leadingIcon = {
                    Icon(Icons.Default.Check, contentDescription = "Accept")
                })
                Spacer(modifier = Modifier.width(8.dp)) // Add space between the AssistChips
                AssistChip(
                    onClick = onDecline,
                    label = { Text(text = "Decline") },
                    leadingIcon = {
                        Icon(Icons.Default.Cancel, contentDescription = "Decline")
                    },
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun prev() {
    NotificationScreen(
        invitations = listOf(
            Invitation(
                fromUserId = "fromUserId",
                toUserId = "toUserId",
                groupId = "groupId",
                status = InvitationStatus.PENDING,
                groupName = "Group 1",
                fromUserName = "User 1",
            ),
            Invitation(
                fromUserId = "fromUserId",
                toUserId = "toUserId",
                groupId = "groupId",
                status = InvitationStatus.PENDING,
                groupName = "Group 1",
                fromUserName = "User 1",
            ),
            Invitation(
                fromUserId = "fromUserId",
                toUserId = "toUserId",
                groupId = "groupId",
                status = InvitationStatus.PENDING,
                groupName = "Group 1",
                fromUserName = "User 1",
            )
        ),
        onAccept = {},
        onDecline = {},
        reload = { }
    )

}
