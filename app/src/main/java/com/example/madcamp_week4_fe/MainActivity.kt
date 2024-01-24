package com.example.madcamp_week4_fe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_week4_fe.analysis.FavorFragment
import com.example.madcamp_week4_fe.databinding.ActivityMainBinding
import com.example.madcamp_week4_fe.home.HomeFragment
import com.example.madcamp_week4_fe.search.SearchFragment
import com.example.madcamp_week4_fe.SharedViewModel


const val TAG_HEART = "heart_fragment"
const val TAG_HOME = "home_fragment"
const val TAG_MYPAGE = "mypage_fragment"

class MainActivity : AppCompatActivity() {
    lateinit var sharedViewModel: SharedViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        setFragment(TAG_HOME, HomeFragment())
        binding.navigationView.selectedItemId = R.id.home

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.heart -> setFragment(TAG_HEART, SearchFragment())
                R.id.home -> setFragment(TAG_HOME, HomeFragment())
                R.id.mypage -> setFragment(TAG_MYPAGE, FavorFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        val currentFragment = manager.findFragmentByTag(tag)
        val newFragment = currentFragment ?: when (tag) {
            TAG_HEART -> SearchFragment()
            TAG_HOME -> HomeFragment()
            TAG_MYPAGE -> FavorFragment()
            else -> HomeFragment()
        }

        hideAllFragments(fragTransaction)

        if (currentFragment == null) {
            fragTransaction.add(R.id.mainFrameLayout, newFragment, tag)
        } else {
            fragTransaction.show(newFragment)
        }

        fragTransaction.commitAllowingStateLoss()
    }

    private fun hideAllFragments(transaction: FragmentTransaction) {
        val heart = supportFragmentManager.findFragmentByTag(TAG_HEART)
        val home = supportFragmentManager.findFragmentByTag(TAG_HOME)
        val mypage = supportFragmentManager.findFragmentByTag(TAG_MYPAGE)

        heart?.let { transaction.hide(it) }
        home?.let { transaction.hide(it) }
        mypage?.let { transaction.hide(it) }
    }

    fun navigateToFragment(tag: String) {
        setFragment(tag, when (tag) {
            TAG_HEART -> SearchFragment()
            TAG_HOME -> HomeFragment()
            TAG_MYPAGE -> FavorFragment()
            else -> HomeFragment()
        })
        binding.navigationView.selectedItemId = when (tag) {
            TAG_HEART -> R.id.heart
            TAG_HOME -> R.id.home
            TAG_MYPAGE -> R.id.mypage
            else -> R.id.home
        }
    }

}