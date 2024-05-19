package com.example.habicted_app.auth

import com.example.habicted_app.auth.data.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Firebase.firestore.collection("users").document(result.user!!.uid)
                .set(hashMapOf("email" to email)).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Firebase.firestore.collection("users").document(result.user!!.uid)
                .set(hashMapOf("email" to email)).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun recoverPass(email: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}