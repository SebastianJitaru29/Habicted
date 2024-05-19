package com.example.habicted_app.data.model

import com.google.firebase.firestore.DocumentSnapshot


enum class InvitationStatus {
    PENDING, ACCEPTED, DECLINED
}

fun safeValueOf(value: String): InvitationStatus {
    return try {
        InvitationStatus.valueOf(value)
    } catch (e: IllegalArgumentException) {
        InvitationStatus.PENDING
    }
}

data class Invitation(
    var id: String? = null,
    val fromUserId: String,
    val toUserId: String,
    val groupId: String,
    val status: InvitationStatus,
    val groupName: String? = null,
    val fromUserName: String? = null,
) {
    constructor(document: DocumentSnapshot) : this(
        id = document.id,
        fromUserId = document.get("fromUserId") as String,
        toUserId = document.get("toUserId") as String,
        groupId = document.get("groupId") as String,
        status = safeValueOf(document.get("status") as String),
        groupName = document.get("groupName") as String?,
        fromUserName = document.get("fromUserName") as String?,
    )

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "fromUserId" to fromUserId,
            "toUserId" to toUserId,
            "groupId" to groupId,
            "status" to status.name,
            "groupName" to groupName,
            "fromUserName" to fromUserName,
        )
    }
}