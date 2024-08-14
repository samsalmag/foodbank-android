package io.github.samsalmag.foodbankandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.samsalmag.foodbankandroid.R
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private val LOGGER = Logger.getLogger(MainActivity::class.java.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavMenu(savedInstanceState)
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
}
