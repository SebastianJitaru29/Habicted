package com.example.habicted_app.data.repository.remote

import android.util.Log
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Invitation
import com.example.habicted_app.data.model.InvitationStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    fun inviteUser(group: Group, toUserId: String) {
        val fromUserId = Firebase.auth.currentUser!!.uid
        val invitation = Invitation(
            fromUserId = fromUserId,
            toUserId = toUserId,
            groupId = group.id,
            groupName = group.name,
            fromUserName = Firebase.auth.currentUser!!.email,
            status = InvitationStatus.PENDING,
        )

        firestore.collection("Invitations").add(invitation.toMap())
            .addOnSuccessListener {
                // Handle success
                Log.d("InvitationRepository", "Invitation sent")
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun acceptInvitation(invitation: Invitation, onInvitationAccepted: () -> Unit) {
        val invitationRef = firestore.collection("Invitations").document(invitation.id!!)
        invitationRef.update("status", InvitationStatus.ACCEPTED.name)
            .addOnSuccessListener {
                onInvitationAccepted()
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun declineInvitation(invitation: Invitation, onDeclined: () -> Unit) {
        val invitationRef = firestore.collection("Invitations").document(invitation.id!!)
        invitationRef.update("status", InvitationStatus.DECLINED.name)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun fetchInvitations(onInvitationsFetched: (List<Invitation>) -> Unit) {
        val userId = Firebase.auth.currentUser!!.uid
        FirebaseFirestore.getInstance().collection("Invitations")
            .whereEqualTo("toUserId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val invitations = emptyList<Invitation>().toMutableList()
                documents.map { doc ->
                    if (Invitation(doc).status == InvitationStatus.PENDING) {
                        invitations += Invitation(doc)
                    }
                }
                onInvitationsFetched(invitations)
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun listenForInvitations(onInvitationReceived: (Invitation) -> Unit) {
        val userId = Firebase.auth.currentUser!!.uid
        FirebaseFirestore.getInstance().collection("Invitations")
            .whereEqualTo("toUserId", userId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                for (doc in snapshots!!) {
                    val invitation = doc.toObject(Invitation::class.java)
                    onInvitationReceived(invitation)
                }
            }
    }
}