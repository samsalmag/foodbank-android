package io.github.samsalmag.foodbankandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.ActivityMainBinding
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private val LOGGER = Logger.getLogger(MainActivity::class.java.name)

    private lateinit var binding: ActivityMainBinding

    private val apiConnectivityChecker = ApiConnectivityChecker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavMenu(savedInstanceState)
        apiConnectivityChecker.checkApiConnectionPeriodically(lifecycleScope, 10000) {
            connectionStatus -> updateApiConnectionText(connectionStatus)
        }
    }

    private fun initBottomNavMenu(savedInstanceState: Bundle?) {
        val bottomNavigationMenu: BottomNavigationView = findViewById(R.id.menu_bottomNavigation)
        bottomNavigationMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomNav_dishes -> {
                    replaceFragment(DishesFragment())
                    true
                }

                else -> false
            }
        }

        // Set the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(DishesFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateApiConnectionText(connection_status: ApiConnectivityStatus) {
        binding.apiConnectionText.text = connection_status.statusText
    }
}
