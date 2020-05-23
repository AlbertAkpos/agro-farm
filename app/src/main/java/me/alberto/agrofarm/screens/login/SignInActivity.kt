package me.alberto.agrofarm.screens.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import me.alberto.agrofarm.R
import me.alberto.agrofarm.databinding.SigninActivityBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: SigninActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signin_activity)

        viewModel = ViewModelProvider(this, SignInViewModel.Factorty()).get(SignInViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observe()

    }

    private fun observe() {
        viewModel.emptyEmail.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.emailInput.error = "Email can't be empty"
        })

        viewModel.emptyPassword.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.passwordInput.error = "Password can't be empty"
        })

        viewModel.validCredentials.observe(this, Observer {
            it ?: return@Observer
            if (it) {

            } else {
                Snackbar.make(binding.root, "Invalid credentials", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
