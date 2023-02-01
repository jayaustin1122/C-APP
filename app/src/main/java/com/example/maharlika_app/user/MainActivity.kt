package com.example.maharlika_app.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.ActivityMainBinding
import com.example.maharlika_app.user.events.EventUserFragment
import com.example.maharlika_app.user.notfications.NotificationFragment
import com.example.maharlika_app.user.profile.ProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        checkUser()



        val eventUserFragment = EventUserFragment()
        val notificationFragment = NotificationFragment()
        val profileFragment = ProfileFragment()

        // to call the initial fragment display in screen
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentMainAdmin.id,eventUserFragment)
            commit()
        }
        // to bind the table layout in buttons navigations

        binding.tabLayout3.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    //accessing button 1 tab
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, eventUserFragment)
                        // creating a backstack
                        addToBackStack(null)
                        commit()
                    }
                }
                else if (tab?.position == 1) {
                    //accessing button 2
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, notificationFragment)
                        // creating a backstack
                        addToBackStack(null)
                        commit()
                    }
                }
                else if (tab?.position == 2) {
                    //accessing button 2
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, profileFragment)
                        // creating a backstack
                        addToBackStack(null)
                        commit()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
    private fun checkUser() {

        val firebaseUser = auth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(this,"   ", Toast.LENGTH_SHORT).show()
        }
        else{
            val email = firebaseUser.email
        }
    }
}