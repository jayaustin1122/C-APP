package com.example.maharlika_app.admin.events

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityAddEventBinding
import com.example.maharlika_app.notifications.NotificationData
import com.example.maharlika_app.notifications.PushNotification
import com.example.maharlika_app.notifications.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val TOPIC = "/topics/allUsers"

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddEventBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var selectedImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.imgAdd.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
        binding.btnSubmit.setOnClickListener {
            validateData()
        }
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, AdminHolderActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.imgAdd.setImageURI(selectedImage)
            }
        }
    }
    var title = ""
    var label = ""
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
        progressDialog.setMessage("Saving Event...")
        progressDialog.show()
        title = binding.etTitleEvents.text.toString()
        description = binding.etDesciptionEvents.text.toString().trim()
        val currentDate = getCurrentDate()
        val timestamp = System.currentTimeMillis()
        val currentTime = getCurrentTime()
        val uid = auth.uid
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["eventsTitle"] = title
        hashMap["eventsDescription"] = description
        hashMap["image"] = imgUrl
        hashMap["currentDate"] = currentDate
        hashMap["currentTime"] = currentTime
        hashMap["id"] = "$timestamp"

        val hashMap2: HashMap<String, Any?> = HashMap()
        hashMap2["uidNotif"] = uid
        hashMap2["NotifTitle"] = title
        hashMap2["NotifDescription"] = description
        hashMap2["imageNotif"] = imgUrl
        hashMap2["currentDateNotif"] = currentDate
        hashMap2["currentTimeNotif"] = currentTime
        hashMap2["id"] = "$timestamp"

        database.getReference("Notifications")
            .child(timestamp.toString())
            .setValue(hashMap2)
        database.getReference("events")
            .child(timestamp.toString())
            .setValue(hashMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    val intent = Intent(this, AdminHolderActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Event Added", Toast.LENGTH_SHORT).show()

                    // Send the notification after successful upload
                    val notification = PushNotification(
                        NotificationData(title, description),
                        TOPIC
                    )
                    sendNotification(notification)
                } else {
                    Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
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
    val TAG = "MainActivity"
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Notification sent successfully")
            } else {
                Log.e(TAG, "Failed to send notification. Error: ${response.errorBody().toString()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending notification: ${e.toString()}")
        }
    }

}