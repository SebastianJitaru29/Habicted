package com.example.habicted_app.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class StorageUtil {

    companion object {
        fun uploadToStorage(uri: Uri, context: Context, type: String, userID: String?) {
            FirebaseAuth.getInstance().currentUser?.let { user ->
                val storage = Firebase.storage
                val storageRef = storage.reference

                val uniqueImgName = UUID.randomUUID().toString()
                val filePath = if (type == "image") "images/$uniqueImgName.jpg" else "videos/$uniqueImgName.mp4"
                val spaceRef: StorageReference = storageRef.child(filePath)

                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val byteArray = inputStream.readBytes()
                        val uploadTask = spaceRef.putBytes(byteArray)
                        uploadTask.addOnFailureListener { exception ->
                            Toast.makeText(context, "Upload Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener { taskSnapshot ->
                            // Get the download URL of the uploaded file
                            val downloadUrlTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                            downloadUrlTask.addOnSuccessListener { downloadUri ->
                                // Write the download URL to the database under the relevant employee userID
                                if (userID != null) {
                                    writeToDatabase(userID, downloadUri.toString())
                                }
                            }.addOnFailureListener { exception ->
                                Toast.makeText(context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }

                            Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }

        private fun writeToDatabase(userID: String, downloadUrl: String) {
            // Here, you can write code to write downloadUrl to your database under the relevant employee userID
            val db = FirebaseFirestore.getInstance()
            val employeeRef = db.collection("users").document(userID)

            // Example: Writing download URL under a field named "photoUrl"
            employeeRef.update("photoUrl", downloadUrl)
                .addOnSuccessListener {
                    // Successfully wrote download URL to the database
                }
                .addOnFailureListener { exception ->
                    // Failed to write download URL to the database
                    // Handle failure gracefully
                }
        }
    }
}
