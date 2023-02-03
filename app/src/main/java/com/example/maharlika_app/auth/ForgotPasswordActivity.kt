package com.example.maharlika_app.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)



        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnSubmit.setOnClickListener {
            validateData()
        }
    }
    private var email = ""
    private fun validateData() {
        email = binding.etForgot.text.toString().trim()

        if( email.isEmpty()){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Format",Toast.LENGTH_SHORT).show()
        }
        else{
            recoveryPass()
        }
    }

    private fun recoveryPass() {
        progressDialog.setMessage("Sending password reset instructions to $email")
        progressDialog.show()
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Sent Success",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to send due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}