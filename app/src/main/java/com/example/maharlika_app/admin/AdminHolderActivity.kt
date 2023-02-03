package com.example.maharlika_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.events.EventAdminFragment
import com.example.maharlika_app.admin.manageAcc.ManageAccountsFragment
import com.example.maharlika_app.admin.news.NewsAdminFragment
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.ActivityAdminHolderBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminHolderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAdminHolderBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        checkUser()
        loadUsersInfo()
        val eventAdminFragment = EventAdminFragment()
        val manageAccAdminFragment = ManageAccountsFragment()
        val newsAdminFragment = NewsAdminFragment()

        // to call the initial fragment display in screen
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentMainAdmin.id,eventAdminFragment)
            commit()
        }
        // to bind the table layout in buttons navigations

        binding.tabLayout3.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    //accessing button 1 tab
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, eventAdminFragment)
                        // creating a backstack
                        addToBackStack(null)
                        commit()
                    }
                } else if (tab?.position == 1) {
                    //accessing button
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, newsAdminFragment)
                        // creating a backstack
                        addToBackStack(null)
                        commit()
                    }
                }
                else if (tab?.position == 2) {
                    //accessing button 2
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.fragmentMainAdmin.id, manageAccAdminFragment)
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
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            Toast.makeText(this,"   ", Toast.LENGTH_SHORT).show()
        }
        else{
            val email = firebaseUser.email
        }
    }
    private fun loadUsersInfo() {
        //reference
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val address = "${snapshot.child("address").value}"
                    val currentDate = "${snapshot.child("currentDate").value}"
                    val currentTime = "${snapshot.child("currentTime").value}"
                    val email = "${snapshot.child("email").value}"
                    val fullName = "${snapshot.child("fullName").value}"
                    val id = "${snapshot.child("id").value}"
                    val image = "${snapshot.child("image").value}"
                    val password = "${snapshot.child("password").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val userType = "${snapshot.child("userType").value}"

                    //set data
                    binding.tvName.text = fullName
                    binding.textView5.text = userType

                    Glide.with(this@AdminHolderActivity)
                        .load(image)
                        .into(binding.mainProfile)


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}