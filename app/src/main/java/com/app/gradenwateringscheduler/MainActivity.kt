package com.app.gradenwateringscheduler
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.gradenwateringscheduler.databinding.ActivityMainBinding

val destinationsWithoutBottomNav = setOf(
    R.id.addPlantViewFragment,
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find NavHostFragment from your activity_main.xml
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        // Connect BottomNavigationView with NavController for automatic handling of fragment switching
        binding.bottomNavigation.setupWithNavController(navController)

        // Hide bottom navigation on certain destinations
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.post {
                binding.bottomNavigation.isVisible = !destinationsWithoutBottomNav.contains(destination.id)
            }
        }

    }
}
