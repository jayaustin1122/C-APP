package com.example.maharlika_app.user.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.maharlika_app.databinding.ActivityEventDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEventDetailBinding

    private var eventId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get event id
        eventId = intent.getStringExtra("id")!!
        loadDetails()
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun loadDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("events")
        ref.child(eventId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data

                    val currentDate = "${snapshot.child("currentDate").value}"
                    val currentTime = "${snapshot.child("currentTime").value}"
                    val eventsDescription = "${snapshot.child("eventsDescription").value}"
                    val eventsTitle = "${snapshot.child("eventsTitle").value}"
                    val id = "${snapshot.child("id").value}"
                    val image = "${snapshot.child("image").value}"
                    val uid = "${snapshot.child("uid").value}"

                    //set data

                    binding.eventTitle.text = eventsTitle
                    binding.newsDes.text = eventsDescription

                    Glide.with(this@EventDetailActivity)
                        .load(image)
                        .into(binding.eventCover)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}