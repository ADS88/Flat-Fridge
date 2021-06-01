package com.example.flatfridge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        setupViews()
    }

    fun setupViews()
    {
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_host) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        //var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        var appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,
            R.id.settingsFragment,
            R.id.addFoodFragment))
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

}