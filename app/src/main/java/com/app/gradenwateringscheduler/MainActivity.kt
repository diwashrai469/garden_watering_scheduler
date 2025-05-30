package com.app.gradenwateringscheduler

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.gradenwateringscheduler.databinding.ActivityMainBinding
import com.app.gradenwateringscheduler.ui.HomeViewFragment
import com.app.gradenwateringscheduler.ui.ScheduleViewFragment
import com.app.gradenwateringscheduler.ui.SettingsViewFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        loadFragment(HomeViewFragment())

        mainBinding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeViewFragment())

                R.id.nav_schedules -> loadFragment(ScheduleViewFragment())

                R.id.nav_settings -> loadFragment(SettingsViewFragment())

                else -> false

            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }


}
