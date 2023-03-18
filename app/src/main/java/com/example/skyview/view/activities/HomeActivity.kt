package com.example.skyview.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.skyview.R
import com.example.skyview.databinding.ActivityHomeBinding
import com.example.skyview.view.fragments.AlertFragment
import com.example.skyview.view.fragments.FavouriteFragment
import com.example.skyview.view.fragments.HomeFragment
import com.example.skyview.view.fragments.SettingFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.favourite -> replaceFragment(FavouriteFragment())
                R.id.alert -> replaceFragment(AlertFragment())
                R.id.setting -> replaceFragment(SettingFragment())
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManger = supportFragmentManager
        val fragmentTransition = fragmentManger.beginTransaction()
        fragmentTransition.replace(R.id.frameLayout, fragment)
        fragmentTransition.commit()
    }
}