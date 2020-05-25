package me.alberto.agrofarm.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import me.alberto.agrofarm.R
import me.alberto.agrofarm.database.FarmDatabase
import me.alberto.agrofarm.databinding.ActivityMainBinding
import me.alberto.agrofarm.repository.FarmRepository
import me.alberto.agrofarm.screens.add_farmer.RC_MAP
import me.alberto.agrofarm.viewmodel.MainViewModel
import me.alberto.agrofarm.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        init()

        viewModel.farmerWithFarms.observe(this, Observer {
            println("""
                
               farmwith: ${it.toString()} 
                
            """)
        })

        navController = Navigation.findNavController(this, R.id.nav_container)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun init() {
        val database = FarmDatabase.getFarmDatabase(applicationContext)
        val repository = FarmRepository(database)
        val viewModelFactorty = MainViewModelFactory(repository)
        viewModel =
            ViewModelProvider(this@MainActivity, viewModelFactorty).get(MainViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MAP && resultCode == Activity.RESULT_OK) {
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }
}
