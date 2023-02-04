package com.example.maharlika_app.admin.events

import android.annotation.SuppressLint
import android.app.Notification
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.admin.news.ModelNews
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.auth.SignUpActivity
import com.example.maharlika_app.databinding.ActivityAddEventBinding
import com.example.maharlika_app.model.UserModel
import com.example.maharlika_app.notifications.NotificationData
import com.example.maharlika_app.notifications.PushNotification
import com.example.maharlika_app.notifications.RetrifitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TOPIC = "/topics/myTopic"

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddEventBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var selectedImage : Uri
    private lateinit var accArrayList : ArrayList<UserModel>
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
            PushNotification(
                NotificationData(title,description),
                TOPIC
            ).also {
                sendNotification(it)
            }


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

        accArrayList = ArrayList()

        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                accArrayList.clear()
                for (data in snapshot.children){
                    val model = data.getValue(UserModel::class.java)
                    title = binding.etTitleEvents.text.toString()
                    description = binding.etDesciptionEvents.text.toString().trim()
                    val currentDate = getCurrentDate()
                    val timestamp = System.currentTimeMillis()
                    val currentTime = getCurrentTime()
                    val uid = model?.uid
                    val hashMap : HashMap<String,Any?> = HashMap()
                    hashMap["uid"] = uid
                    hashMap["eventsTitle"] = title
                    hashMap["eventsDescription"] = description
                    hashMap["image"] = imgUrl
                    hashMap["currentDate"] = currentDate
                    hashMap["currentTime"] = currentTime
                    hashMap["id"] = "$timestamp"


                    database.getReference("Users")
                        .child(model?.uid!!)
                        .child("eventNotifications")
                        .child(timestamp.toString())
                        .setValue(hashMap)
                    database.getReference("events")
                        .child(timestamp.toString())
                        .setValue(hashMap)
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                progressDialog.dismiss()
                                    startActivity(Intent(this@AddEventActivity,AdminHolderActivity::class.java))
                            }
                            else{

                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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