package com.codewithshadow.filmrave.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.codewithshadow.filmrave.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.homeFragment) {
            finish()
        }
        super.onBackPressed()
    }


}