package com.example.clubapp.signin
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun signIn(authViewModel: SignInViewModel): Boolean {
        val token = if (isSignedIn()) {
            firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
        } else {
            try {
                val result = buildCredentialRequest()
                handleSignIn(result)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                null
            }
        }

        return token?.let {
//            println("GoogleAuthClient: Logging in with token = $it")
            authViewModel.login(it)
            true
        } ?: false
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): String? {
        val credential = result.credential

        return if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val tokenId = tokenCredential.idToken

                val authCredential = GoogleAuthProvider.getCredential(tokenId, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                if (authResult.user != null) {
                    val firebaseToken = authResult.user?.getIdToken(true)?.await()?.token
                    return firebaseToken
                }
                null
            } catch (e: GoogleIdTokenParsingException) {
                null
            }
        } else {
            null
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("290590681565-vkacsc02u4i10mg5ppmgjlfel6t8i222.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()
        return credentialManager.getCredential(context, request)
    }

    suspend fun signOut() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        firebaseAuth.signOut()
    }
}
