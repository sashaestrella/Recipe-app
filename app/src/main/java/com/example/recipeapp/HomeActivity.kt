package com.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.codingwithme.recipeapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import android.view.MenuItem
import com.example.recipeapp.fragments.FavouriteFragment
import com.example.recipeapp.fragments.HomeFragment
import java.lang.IllegalArgumentException


class HomeActivity : BaseActivity() {
    private var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView?
        setupBottomMenu()

        replaceFragment(HomeFragment.newInstance(),false)
    }

    private fun setupBottomMenu() {
        bottomNavigationView?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.recetas -> replaceFragment(HomeFragment.newInstance(), false)
                R.id.favoritos -> replaceFragment(FavouriteFragment.newInstance(), false)
                R.id.informacion -> replaceFragment(HomeFragment.newInstance(), false)
                else -> throw IllegalArgumentException("item not implemented : " + item.itemId)
            }
            true
        })
        //setear aquí para que el listener muestre el fragment inicial al cargarse la pantalla
        bottomNavigationView?.setSelectedItemId(R.id.recetas)
    }

    fun replaceFragment(fragment: Fragment, istransition: Boolean){
        val fragmentTransition = supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.add(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragments = supportFragmentManager.fragments
        if (fragments.size == 0){
            finish()
        }
    }
}

