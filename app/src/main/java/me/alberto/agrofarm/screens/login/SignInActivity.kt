package me.alberto.agrofarm.screens.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.alberto.agrofarm.R
import me.alberto.agrofarm.activity.MainActivity
import me.alberto.agrofarm.databinding.SigninActivityBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: SigninActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signin_activity)

        viewModel =
            ViewModelProvider(this, SignInViewModel.Factorty()).get(SignInViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observe()

    }

    private fun observe() {
        viewModel.emptyEmail.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.emailInput.error = "Required field"
        })

        viewModel.emptyPassword.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.passwordInput.error = "Required field"
        })

        viewModel.validCredentials.observe(this, Observer {
            it ?: return@Observer
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                binding.passwordField.error = "Invalid email or password"
            }
        })
    }
}
