package com.example.maharlika_app.admin.events

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.ActivityAddEventBinding
import com.example.maharlika_app.model.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddEventBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private var imageUri : Uri? = null
    private val selectedImage =registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.imgAdd.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.imgAdd.setOnClickListener {
            selectedImage.launch("image/*")
        }
        binding.btnSubmit.setOnClickListener {
            validateData()
        }

    }
    var title = ""
    var description = ""
    private fun validateData() {
        title = binding.etTitleEvents.text.toString()
        description = binding.etDesciptionEvents.text.toString().trim()
        if (title.isEmpty()){
            Toast.makeText(this,"Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else if (description.isEmpty()){
            Toast.makeText(this,"Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
        }
    }
    private fun uploadImage() {
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()
        val uid = auth.uid

        val storageRef = FirebaseStorage.getInstance().getReference("EventProfile")
            .child(title)
            .child("profile.jpg")


        storageRef.putFile(imageUri!!)
            .addOnCompleteListener{
                storageRef.downloadUrl.addOnSuccessListener {
                    updateProfile(it)
                }.addOnFailureListener {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            }.addOnFailureListener {
                Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }



    }


    private fun updateProfile(imageUrl: Uri?) {
        progressDialog.setMessage("Saving Event...")
        progressDialog.show()
        title = binding.etTitleEvents.text.toString()
        description = binding.etDesciptionEvents.text.toString().trim()
        val currentDate = getCurrentDate()
        val timestamp = System.currentTimeMillis()
        val currentTime = getCurrentTime()

        val data = EventModel(
            id = auth.uid!!,
            eventsTitle = title,
            eventsDescription = description,
            timestamp = timestamp,
            uid = auth.uid!!,
            image = imageUrl.toString(),
            currentDate = currentDate,
            currentTime = currentTime
        )
        database.getReference("events")
            .child(title)
            .setValue(data)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    progressDialog.dismiss()
                    startActivity(Intent(this,AdminHolderActivity::class.java))
                    finish()
                    Toast.makeText(this,"Account Created", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun getCurrentTime(): String {
        val tz = TimeZone.getTimeZone("GMT+08:00")
        val c = Calendar.getInstance(tz)
        val hours = String.format("%02d", c.get(Calendar.HOUR))
        val minutes = String.format("%02d", c.get(Calendar.MINUTE))
        return "$hours:$minutes"
    }


    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val currentDateObject = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(currentDateObject)
    }
}