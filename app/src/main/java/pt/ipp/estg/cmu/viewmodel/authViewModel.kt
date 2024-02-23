package pt.ipp.estg.cmu.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.cmu.R

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> get() = _authState

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient by lazy {
        provideGoogleSignInClient(
            application
        )
    }

    private fun provideGoogleSignInClient(application: Application): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(application, gso)
    }

    private val _googleSignInIntent = MutableLiveData<Intent>()
    val googleSignInIntent: LiveData<Intent> get() = _googleSignInIntent


    fun startGoogleSignIn(activity: Activity) {
        _googleSignInIntent.value = googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
                firebaseAuthWithGoogle(account.idToken!!)
                _authState.value = true
            } catch (e: Exception) {
                _authState.value = false
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            try {
                val result = firebaseAuth.signInWithCredential(credential).await()

                // Check if the authentication was successful
                if (result.user != null) {
                    // Authentication with Google was successful
                    // You can perform actions like updating UI, navigating to the main screen, etc.
                    Log.d(TAG, "Auhentication with Google successful")
                } else {
                    // Authentication with Google failed
                    Log.d(TAG, "Authentication with Google failed: User is null")
                }
            } catch (e: Exception) {
                // Handle the exception, e.g., display an error message
                Log.e(TAG, "Authentication with Google failed", e)
            }
        }
    }


    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                if (result.user != null) {
                    onSignInSuccess()
                } else {
                    onSignInFailed()
                }
            } catch (e: Exception) {
                onSignInFailed()
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFailed: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                if (result.user != null) {
                    onSignUpSuccess()
                } else {
                    onSignUpFailed()
                }
            } catch (e: Exception) {
                onSignUpFailed()
            }
        }
    }


    companion object {
        private val TAG = "AuthViewModel1"
    }
}
