package com.example.maharlika_app.admin.news

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityAddNewsBinding
import com.example.maharlika_app.notifications.NotificationData
import com.example.maharlika_app.notifications.PushNotification
import com.example.maharlika_app.notifications.RetrifitInstance
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
const val TOPIC = "/topics/myTopic"
class AddNewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNewsBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var selectedImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        //init
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

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
    var description = ""
    private fun validateData() {
        title = binding.etTitleNews.text.toString()
        description = binding.etDesciptionNews.text.toString().trim()
        if (title.isEmpty()){
            Toast.makeText(this,"Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else if (description.isEmpty()){
            Toast.makeText(this,"Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
            PushNotification(
                NotificationData(title,description),
                com.example.maharlika_app.admin.events.TOPIC
            ).also {
                sendNotification(it)
            }

        }
    }
    private fun uploadImage() {
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()

        val reference = storage.reference.child("NewsProfile")
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
        progressDialog.setMessage("Saving News...")
        progressDialog.show()
        title = binding.etTitleNews.text.toString()
        description = binding.etDesciptionNews.text.toString().trim()
        val currentDate = getCurrentDate()
        val timestamp = System.currentTimeMillis()
        val currentTime = getCurrentTime()
        val uid = auth.uid
        val hashMap : HashMap<String,Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["newsTitle"] = title
        hashMap["newsDescription"] = description
        hashMap["image"] = imgUrl
        hashMap["currentDate"] = currentDate
        hashMap["currentTime"] = currentTime
        hashMap["id"] = "$timestamp"

        database.getReference("news")
            .child(timestamp.toString())
            .setValue(hashMap)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    progressDialog.dismiss()
                    val intent = Intent(this, AdminHolderActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"News Added", Toast.LENGTH_SHORT).show()
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
    val TAG = "MainActivity"
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        try{
            val response = RetrifitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }
            else{
                Log.e(TAG,response.errorBody().toString() )
            }
        }
        catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }
}