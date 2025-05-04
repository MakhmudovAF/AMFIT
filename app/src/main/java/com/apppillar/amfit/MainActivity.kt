package com.apppillar.amfit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.apppillar.amfit.databinding.ActivityMainBinding
import com.apppillar.core.data.TokenDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var tokenDataStore: TokenDataStore

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
            val token = tokenDataStore.token.firstOrNull()
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as NavHostFragment
            val navController = navHostFragment.navController
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(
                if (!token.isNullOrBlank()) R.id.homeFragment else com.apppillar.feature_auth.R.id.auth_nav_graph
            )
            navController.graph = graph
            delay(1)
            isTokenChecked = true
        }
    }
}