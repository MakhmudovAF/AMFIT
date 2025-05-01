package com.apppillar.amfit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.apppillar.amfit.databinding.ActivityMainBinding
import com.apppillar.core.data.TokenDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Проверка токена при запуске
        lifecycleScope.launch {
            tokenDataStore.token.firstOrNull()?.let { token ->
                if (!token.isNullOrBlank()) {
                    // Токен найден — переходим на главный экран
                    val navController = findNavController(R.id.fragment_container_view)
                    navController.navigate(R.id.homeFragment)
                }
                // иначе остаёмся в auth_nav_graph
            }
        }
    }
}