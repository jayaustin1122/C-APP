package com.example.maharlika_app.user.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.maharlika_app.databinding.ActivityNewsDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewsDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsDetailsBinding
    private var newsId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get event id
        newsId = intent.getStringExtra("id")!!
        loadDetails()
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun loadDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("news")
        ref.child(newsId)
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

                    Glide.with(this@NewsDetailsActivity)
                        .load(image)
                        .into(binding.newsCover)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}