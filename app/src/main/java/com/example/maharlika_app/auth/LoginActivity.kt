package com.example.maharlika_app.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.maharlika_app.R
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityLoginBinding
import com.example.maharlika_app.user.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.btnSignIn.setOnClickListener {
            validateData()
        }
        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
            finish()
        }
    }
    var email = ""
    var pass = ""

    private fun validateData() {
        email = binding.etEmailSignIn.text.toString().trim()
        pass = binding.etPasswordSignIn.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email
            Toast.makeText(this,"Email Invalid", Toast.LENGTH_SHORT).show()
        }
        else if (pass.isEmpty()){

        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        progressDialog.setMessage("Logging In...")
        progressDialog.show()


        auth.signInWithEmailAndPassword(email,pass)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        progressDialog.setTitle("Checking user...")

        val firebaseUser = auth.currentUser!!

        val dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val userType = snapshot.child("userType").value

                    if (userType == "member") {
                        Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()

                    } else if (userType == "admin") {
                        Toast.makeText(applicationContext, "Welcome Admin", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, AdminHolderActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}