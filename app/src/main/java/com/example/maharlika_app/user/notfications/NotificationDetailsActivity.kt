package com.example.maharlika_app.user.notfications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.maharlika_app.databinding.ActivityNotificationDetailsBinding
import com.example.maharlika_app.user.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationDetailsBinding
    private var notifID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get event id
        notifID = intent.getStringExtra("id")!!
        loadDetails()
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun loadDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Notifications")
        ref.child(notifID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data

                    val currentDate = "${snapshot.child("currentDate").value}"
                    val currentTime = "${snapshot.child("currentTime").value}"
                    val newsDescription = "${snapshot.child("newsDescription").value}"
                    val newsTitle = "${snapshot.child("newsTitle").value}"
                    val id = "${snapshot.child("id").value}"
                    val image = "${snapshot.child("image").value}"
                    val uid = "${snapshot.child("uid").value}"

                    //set data

                    binding.newsTitle.text = newsTitle
                    binding.newsDes.text = newsDescription

                    Glide.with(applicationContext)
                        .load(image)
                        .into(binding.newsCover)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}