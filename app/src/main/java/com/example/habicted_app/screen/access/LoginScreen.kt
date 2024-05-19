package com.example.habicted_app.screen.access

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habicted_app.R
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.screen.EmailInputField
import com.example.habicted_app.screen.PasswordInputField
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.example.habicted_app.ui.theme.righteousFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onLogIn: () -> Unit,
    onSignUp: () -> Unit,
    onForgotedPassword: () -> Unit,
    state: AuthState,
    tryLogin: (String, String) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthCompleted = { result ->
            user = result.user
        },
        onAuthError = { error ->
            Log.d("TAG", "WelcomeScreen: ${error.message}")
            user = null
        }
    )
    val token = stringResource(id = R.string.webIDGoogleSignIn)
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row {
            //TODO: Add logo
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = TextStyle(fontSize = 40.sp, fontFamily = righteousFamily),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            EmailInputField(
                modifier = Modifier.fillMaxWidth(),
                email = email,
                onEmailChange = { email = it }
            )

            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                password = password,
                onPasswordChange = { password = it },
                label = {
                    Text(
                        text = stringResource(id = R.string.password),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                isPasswordValid = {
                    when {
                        password.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
                        else -> null
                    }
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    if(user == null) {
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                        Log.d("GoogleAuth", "SignedIn correctly: ${user?.email}")
                        //After sign in is done app should go to HomeScreen
                    } else {
                        //User already registered app should go directly to HomeScreen
                        Log.d("GoogleAuth", "WelcomeScreen: ${user?.email}")
                        Log.d("GoogleAuth", "WelcomeScreen: ${user?.displayName}")
                    }
                },
            ) {
                Text(text = stringResource(id = R.string.googlesignin))
            }
        }
        ActionButtons(
            onLogIn = onLogIn,
            onSignUp = onSignUp,
            onForgotedPassword = onForgotedPassword,
            email = email,
            password = password,
            state = state,
            tryLogin = tryLogin
        )
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthCompleted: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
)
: ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleAuth", "rememberFirebaseAuthLauncher: ${account.id}")
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthCompleted(authResult) //triggered if everything goes correctly
            }
        } catch (e: ApiException) {
            Log.d("GoogleAuth", "rememberFirebaseAuthLauncher: ${e.message}")
            onAuthError(e)
        }
    }

}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onLogIn: () -> Unit,
    onSignUp: () -> Unit,
    onForgotedPassword: () -> Unit,
    email: String,
    password: String,
    state: AuthState,
    tryLogin: (String, String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onForgotedPassword,
            ) {
                Text(text = stringResource(id = R.string.forgotpass))
            }
        }

        Button(
            onClick = {
                scope.launch {
                    tryLogin(email, password)
                }
            },
            shape = RoundedCornerShape(49.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.paddingFromBaseline(top = 24.dp),
        )

        OutlinedButton(
            onClick = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
    LaunchedEffect(key1 = state.isSuccess) {
        scope.launch {
            if (state.isSuccess?.isNotEmpty() == true) {
                val success = state.isSuccess
                Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
                onLogIn()
            }
        }
    }

    LaunchedEffect(key1 = state.isError) {
        scope.launch {
            if (state.isError?.isNotEmpty() == true) {
                val error = state.isError
                Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
            }
        }
    }

}

//@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewEmailOutlinedTextField() {
//    HabictedAppTheme {
//        WelcomeScreen(
//            onLogIn = {},
//            onSignUp = {},
//            onForgotedPassword = {},
//            state = AuthState(),
//            tryLogin = { _, _ -> })
//    }
//}

