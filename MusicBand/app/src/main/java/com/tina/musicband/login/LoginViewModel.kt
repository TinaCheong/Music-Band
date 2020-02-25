package com.tina.musicband.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Logger
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.network.LoadApiStatus
import com.tina.musicband.util.Util.getString

class LoginViewModel (private val repository: MusicBandRepository) : ViewModel(){

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // Handle leave login
    private val _loginFacebook = MutableLiveData<Boolean>()

    val loginFacebook: LiveData<Boolean>
        get() = _loginFacebook

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    lateinit var fbCallbackManager: CallbackManager

    private var auth = FirebaseAuth.getInstance()


    fun fbLogin(user: User){

        _status.value = LoadApiStatus.LOADING

        fbCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(fbCallbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                MusicBandApplication.instance.user.fbToken = loginResult.accessToken.toString()
                handleFacebookAccessToken(user, loginResult.accessToken)
            }

            override fun onCancel() { _status.value = LoadApiStatus.ERROR }

            override fun onError(exception: FacebookException) {
                exception.message?.let {
                    _error.value = if (it.contains("ERR_INTERNET_DISCONNECTED")) {
                        getString(R.string.internet_not_connected)
                    } else {
                        it
                    }
                }
                _status.value = LoadApiStatus.ERROR
            }
        })

        checkFbLogin()
    }

    private val _didFinishLogin = MutableLiveData<Boolean>()
    val didFinishLogin: LiveData<Boolean>
        get() = _didFinishLogin

    fun doneNavigateToAvatarPage() {
        _didFinishLogin.value = null
    }

    private fun handleFacebookAccessToken(user: User, token: AccessToken) {

        if (user.fbToken != null) {

            val credential = FacebookAuthProvider.getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.let {
                            UserManager.userToken = it.uid
                            UserManager.userName = it.displayName
                            user.username = it.displayName
                            user.userId = it.uid
                            FirebaseFirestore.getInstance().collection("users")
                                .document(it.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d("Firebase User Data", "Input Success")
                                    _didFinishLogin.value = true
                                }.addOnFailureListener {
                                    Log.d("Firebase User Data", "Input Fail")
                                }
                        }

                        Log.d("Facebook Login", "signInWithCredential:success")

                    } else {
                        Log.w("Facebook Login", "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            MusicBandApplication.instance.applicationContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }

    private fun checkFbLogin() {
        _loginFacebook.value = true
    }

    fun onLoginFbCompleted() {
        _loginFacebook.value = null
    }

}