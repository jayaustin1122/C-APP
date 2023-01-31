package com.example.maharlika_app.admin.events

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.ActivityEditEventBinding
import com.google.firebase.database.FirebaseDatabase

class EditEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditEventBinding
    private var eventId = ""
    private var eventTitle = ""
    private var eventDesc = ""

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get id to edit events
        eventId = intent.getStringExtra("id")!!
        eventTitle = intent.getStringExtra("eventTitle")!!
        eventDesc = intent.getStringExtra("eventdescription")!!


        binding.etTitleEventsEdit.setText(eventTitle)
        binding.etDesciptionEventsEdit.setText(eventDesc)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnUpdate.setOnClickListener {
            updateData()

        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun updateData() {
        //get data
        eventsTitle = binding.etTitleEventsEdit.text.toString()
        eventsDescription = binding.etDesciptionEventsEdit.text.toString()

        if (eventsTitle.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
        }
        else if (eventsDescription.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        }
        else{
            updateNewData()
        }

    }

    private var eventsTitle = ""
    private var eventsDescription = ""
    private fun updateNewData() {
        progressDialog.setMessage("Updating Event")
        progressDialog.show()

        //set up to db
        val hashMap = HashMap<String, Any?>()

        hashMap["eventsTitle"] = "$eventsTitle"
        hashMap["eventsDescription"] = "$eventsDescription"

        val dbRef = FirebaseDatabase.getInstance().getReference("events")
        dbRef.child(eventId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Event Updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminHolderActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Unable to update due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }


    }
}