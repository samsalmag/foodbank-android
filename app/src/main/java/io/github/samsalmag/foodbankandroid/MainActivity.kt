package io.github.samsalmag.foodbankandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.ActivityMainBinding
import io.github.samsalmag.foodbankandroid.fragment.DishesFragment
import io.github.samsalmag.foodbankandroid.fragment.GroceryListFragment
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
            val selectedFragment = when (item.itemId) {
                R.id.bottomNav_dishes -> DishesFragment()
                R.id.bottomNav_groceryList -> GroceryListFragment()
                else -> null
            }

            // Only switch fragments if the selected fragment is not null
            selectedFragment?.let {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment != null && currentFragment::class != selectedFragment::class) {
                    // The selected fragment is not the current fragment, so replace it
                    replaceFragment(it)
                }
            }

            true
        }

        // Set the initial fragment
        val defaultSelectedFragment = DishesFragment()
        if (savedInstanceState == null) {
            replaceFragment(defaultSelectedFragment)
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
