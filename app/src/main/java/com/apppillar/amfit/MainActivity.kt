package com.apppillar.amfit

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.apppillar.amfit.databinding.ActivityMainBinding
import com.apppillar.core.navigation.FragmentDataListener
import com.apppillar.core.storage.DataStorePrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentDataListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var dataStorePrefs: DataStorePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var isTokenChecked = false
        splashScreen.setKeepOnScreenCondition { !isTokenChecked }

        bottomNavigationView = binding.bottomNavigationView
        navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as
                NavHostFragment).navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                "LoginFragment", "RegisterFragment", "UserProfileFragment" -> {
                    bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            val token = dataStorePrefs.token.firstOrNull()
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(
                if (!token.isNullOrBlank()) {
                    com.apppillar.feature_home.R.id.home_nav_graph
                } else {
                    com.apppillar.feature_auth.R.id.auth_nav_graph
                }
            )
            navController.graph = graph
            delay(1)
            isTokenChecked = true
        }
    }

    override fun passWorkoutIdEdit(workoutId: Long) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = bundleOf("workoutId" to workoutId)
        navController.navigate(R.id.workoutEditFragment, bundle)
    }

    override fun passWorkoutIdStart(workoutId: Long) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = bundleOf("workoutId" to workoutId)
        navController.navigate(R.id.workoutStartFragment, bundle)
    }
}

/*
package com.apppillar.amfit

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.apppillar.amfit.databinding.ActivityMainBinding
import com.apppillar.core.navigation.FragmentDataListener
import com.apppillar.core.storage.DataStorePrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentDataListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var dataStorePrefs: DataStorePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isTokenChecked = false
        splashScreen.setKeepOnScreenCondition { !isTokenChecked }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val token = dataStorePrefs.token.firstOrNull()
            bottomNavigationView = binding.bottomNavigationView
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as NavHostFragment
            val navController = navHostFragment.navController
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.label) {
                    "LoginFragment", "RegisterFragment" -> {
                        bottomNavigationView.visibility = View.GONE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
            bottomNavigationView.setupWithNavController(navController)
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(
                if (!token.isNullOrBlank()) R.id.homeFragment else com.apppillar.feature_auth.R.id.auth_nav_graph
            )
            navController.graph = graph
            delay(1)
            isTokenChecked = true
        }
    }

    override fun passWorkoutIdEdit(workoutId: Long) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = bundleOf("workoutId" to workoutId)
        navController.navigate(R.id.workoutEditFragment, bundle)
    }

    override fun passWorkoutIdStart(workoutId: Long) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = bundleOf("workoutId" to workoutId)
        navController.navigate(R.id.workoutStartFragment, bundle)
    }
}*/
