package com.esteban.blogapp.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.esteban.blogapp.R
import com.esteban.blogapp.view.localPosts.LocalPostsFragment
import com.esteban.blogapp.view.remoteNews.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    lateinit var fragmentHome: HomeFragment
    lateinit var fragmentLocal: LocalPostsFragment
    lateinit var activeFragment: Fragment
    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        fragmentManager = supportFragmentManager
        fragmentHome = HomeFragment()
        fragmentLocal = LocalPostsFragment()
        activeFragment = fragmentHome

        val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            (object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.navigation_home -> {
                            fragmentManager.beginTransaction().hide(activeFragment).show(fragmentHome).commit()
                            activeFragment = fragmentHome
                            this@HomeActivity.title = resources.getText(R.string.title_home)
                            return true
                        }
                        R.id.navigation_dashboard -> {
                            fragmentManager.beginTransaction().hide(activeFragment).show(fragmentLocal).commit()
                            activeFragment = fragmentLocal
                            this@HomeActivity.title = resources.getText(R.string.title_dashboard)
                            return true
                        }
                    }
                    return false
                }
            })

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fragmentManager.beginTransaction().add(R.id.main_container, fragmentLocal, fragmentLocal.javaClass.simpleName).hide(fragmentLocal).commit()
        fragmentManager.beginTransaction().add(R.id.main_container, fragmentHome, fragmentHome.javaClass.simpleName).commit()
    }

    fun showLoading() {
        progressBar1.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progressBar1.visibility = View.GONE
    }
}