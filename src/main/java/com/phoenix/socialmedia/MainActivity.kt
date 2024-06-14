package com.phoenix.socialmedia

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.phoenix.socialmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    fun actionBar(title: String, icon: Int, showBarState: Boolean, showBackState: Boolean = false){
        supportActionBar?.title = title
        if(showBarState){
        supportActionBar?.show()}
        else{
            supportActionBar?.hide()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackState)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{

                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up BottomNavigationView
        val navView: BottomNavigationView = binding.bottomNavBar

        // Find NavHostFragment and NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the BottomNavigationView with the NavController
        navView.setupWithNavController(navController)
    }

    fun getNavigationBar(): BottomNavigationView{
        return  binding.bottomNavBar
    }



}
