package com.internshala.notes.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

class GoogleSignInHelper(context: Context) {
    private val _gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private val _googleSignInClient = GoogleSignIn.getClient(context, _gso)

    fun getSignInIntentLauncher(
        fragment: Fragment,
        onActivityResultCallback: (
            completedTask: Task<GoogleSignInAccount>
        ) -> Unit
    ): ActivityResultLauncher<Intent> {
        val googleSignInIntentContract = object : ActivityResultContract<Intent, Intent?>() {
            override fun createIntent(context: Context, signInItent: Intent): Intent {
                return signInItent
            }

            override fun parseResult(resultCode: Int, data: Intent?): Intent? {
                return data
            }
        }

        val googleSignInResultCallback = ActivityResultCallback<Intent?> {
            it?.let { data ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Request viewModel to handle the result
                onActivityResultCallback(task)
            }
        }

        return fragment.registerForActivityResult(
            googleSignInIntentContract,
            googleSignInResultCallback
        )
    }

    /**
     * @param signInIntentLauncher, launcher returned by getSignInIntentLauncher()
     * */
    fun signIn(signInIntentLauncher: ActivityResultLauncher<Intent>) {
        // Remove current user, if any
        signOut()
        signInIntentLauncher.launch(_googleSignInClient.signInIntent)
    }

    fun signOut() {
        _googleSignInClient.signOut()
    }
}