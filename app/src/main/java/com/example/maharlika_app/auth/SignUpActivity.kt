package com.example.maharlika_app.auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.maharlika_app.R
import com.example.maharlika_app.databinding.ActivitySignUpBinding
import com.example.maharlika_app.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private var imageUri : Uri? = null
    private val selectedImage =registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.userImg.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.userImg.setOnClickListener {
            selectedImage.launch("image/*")
        }
        binding.btnSignUp.setOnClickListener {
            validateData()
        }
    }
    private var email = ""
    private var pass = ""
    private var pass2 = ""
    private var fullname = ""
    private var address = ""
    private var userType = "member"

    private fun validateData() {
        email = binding.etEmailSignUp.text.toString().trim()
        pass = binding.etPassSignUp.text.toString().trim()
        pass2 = binding.etPassSignUp.text.toString().trim()
        fullname = binding.etFullNameSignUp.text.toString().trim()
        address = binding.etAddreddSignUp.text.toString().trim()

        if (email.isEmpty()){
            Toast.makeText(this,"Enter Your Email...", Toast.LENGTH_SHORT).show()
        }
        else if (pass.isEmpty()){
            Toast.makeText(this,"Enter Your Password...", Toast.LENGTH_SHORT).show()
        }
        else if (pass2.isEmpty()){
            Toast.makeText(this,"Confirm Password...", Toast.LENGTH_SHORT).show()
        }
        else if (pass != pass2){
            Toast.makeText(this,"Password Doesn't Match...", Toast.LENGTH_SHORT).show()
        }
        else if (fullname.isEmpty()){
            Toast.makeText(this,"Enter Your Fullname...", Toast.LENGTH_SHORT).show()
        }
        else if (address.isEmpty()){
            Toast.makeText(this,"Enter Your Address...", Toast.LENGTH_SHORT).show()
        }
        else if (!binding.checkBox.isChecked){
            Toast.makeText(this,"Please Accept terms and Conditions..", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }
    private fun createUserAccount() {
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        auth.createUserWithEmailAndPassword(email,pass)

            .addOnSuccessListener {
                // if user successfully created ()
                uploadImage()
            }
            .addOnFailureListener { e ->
                //if the user fialef creating account
                progressDialog.dismiss()
                Toast.makeText(this,"Failed Creating Account or ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()
        val uid = auth.uid

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(fullname)
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

        progressDialog.setMessage("Saving Account...")
        progressDialog.show()
        email = binding.etEmailSignUp.text.toString().trim()
        pass = binding.etPassSignUp.text.toString().trim()
        pass2 = binding.etPassSignUp.text.toString().trim()
        fullname = binding.etFullNameSignUp.text.toString().trim()
        address = binding.etAddreddSignUp.text.toString().trim()
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()
        val data = UserModel(
            email = email,
            password = pass,
            fullName = fullname,
            address = address,
            profileImage = imageUrl.toString(),
            userType = "member",
            currentDate = currentDate,
            currentTime = currentTime,

        )

        database.getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(data)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    progressDialog.dismiss()
                    startActivity(Intent(this,LoginActivity::class.java))
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