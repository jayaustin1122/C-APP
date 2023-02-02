package com.example.maharlika_app.admin.events

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.ActivityEditEventBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class EditEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditEventBinding
    private var eventId = ""
    private var eventTitle = ""
    private var eventDesc = ""
    private var image = ""
    private lateinit var selectedImage : Uri
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var progressDialog : ProgressDialog

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //get id to edit events
        eventId = intent.getStringExtra("id")!!
        eventTitle = intent.getStringExtra("eventTitle")!!
        eventDesc = intent.getStringExtra("eventdescription")!!
        image = intent.getStringExtra("image")!!


        binding.etTitleEventsEdit.setText(eventTitle)
        binding.etDesciptionEventsEdit.setText(eventDesc)

        Glide.with(this)
            .load(image)
            .into(binding.imageView)

        binding.btnUpdate.setOnClickListener {
            updateData2()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.imageView.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
            binding.btnUpdate.setTransitionVisibility(View.GONE)
            binding.btnUpdate2.setTransitionVisibility(View.VISIBLE)
        }
        binding.btnUpdate2.setOnClickListener {
            updateData()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.imageView.setImageURI(selectedImage)
            }
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
            uploadImage()
        }

    }
    private fun updateData2() {
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
            uploadInfo2()
        }

    }
    private fun uploadImage() {
        progressDialog.setMessage("Uploading New Image...")
        progressDialog.show()

        val reference = storage.reference.child("EventProfile")
            .child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
                }
            }
        }

    }

    private fun uploadInfo(imgUrl: String) {
        progressDialog.setMessage("Updating Event")
        progressDialog.show()

        //set up to db
        val hashMap = HashMap<String, Any?>()

        hashMap["eventsTitle"] = "$eventsTitle"
        hashMap["eventsDescription"] = "$eventsDescription"
        hashMap["image"] = imgUrl

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
    private fun uploadInfo2() {
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

    private var eventsTitle = ""
    private var eventsDescription = ""

}