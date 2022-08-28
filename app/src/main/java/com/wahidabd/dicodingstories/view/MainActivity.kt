package com.wahidabd.dicodingstories.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.databinding.ActivityMainBinding
import com.wahidabd.dicodingstories.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ThemeViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        navController = navHost.navController
        binding.bottomNav.setupWithNavController(navController)

        viewModel.getTheme().observe(this){
            if (it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        navController.addOnDestinationChangedListener{_, dest, _ ->
            when(dest.id){
                R.id.homeFragment, R.id.settingFragment, R.id.mapsFragment
                    -> binding.bottomNav.visibility = View.VISIBLE
                else -> binding.bottomNav.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val dest = navController.currentDestination
        Timber.d("Nav Controller ${dest?.id} -> ${dest?.label}")

        return if (dest != null){
            when(dest.label){
                FRAGMENT_DETAIL -> navController.navigateUp()
                else -> navController.navigateUp()
            }
        }else navController.navigateUp()
    }


    companion object{
        const val FRAGMENT_DETAIL = "fragment_detail"
    }
}