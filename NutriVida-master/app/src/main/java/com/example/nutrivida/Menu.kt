package com.example.nutrivida

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.security.Principal

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val vpVisor = findViewById<ViewPager2>(R.id.vpVisor)

        val viewPagerAdapter = ViewPagerAdapter(this) // Envio de parametros que se usuara en los fragments
        vpVisor.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, vpVisor) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "HOME"
                    tab.setIcon(R.drawable.home)
                }
                1 -> {
                    tab.text = "CALORIAS"
                    tab.setIcon(R.drawable.calorias)
                }
                2 -> {
                    tab.text = "ACTIVIDAD"
                    tab.setIcon(R.drawable.actividad)
                }
                3 -> {
                    tab.text = "PERFIL"
                    tab.setIcon(R.drawable.perfil)
                }
            }
        }.attach()
    }
}