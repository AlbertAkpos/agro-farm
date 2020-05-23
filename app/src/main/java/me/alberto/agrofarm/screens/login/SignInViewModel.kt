package me.alberto.agrofarm.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignInViewModel : ViewModel() {

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private val _emptyEmail = MutableLiveData<Boolean>()
    val emptyEmail: LiveData<Boolean>
        get() = _emptyEmail

    private val _emptyPassword = MutableLiveData<Boolean>()
    val emptyPassword: LiveData<Boolean>
        get() = _emptyPassword

    private val _validCredentials = MutableLiveData<Boolean>()
    val validCredentials: LiveData<Boolean>
        get() = _validCredentials


    fun validateCredentials() {
        when {
            email.value.isNullOrEmpty() && password.value.isNullOrEmpty() -> {
                _emptyPassword.value = true
                _emptyEmail.value = true
                return
            }

            email.value.isNullOrEmpty() -> _emptyEmail.value = true
            password.value.isNullOrEmpty() -> {
                _emptyPassword.value = true
                return
            }


            email.value != "test@theagromall.com" || password.value != "password" -> {
                _validCredentials.value = false
                return
            }

            else -> _validCredentials.value = true


        }
    }


    class Factorty : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel() as T
            }

            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }

}