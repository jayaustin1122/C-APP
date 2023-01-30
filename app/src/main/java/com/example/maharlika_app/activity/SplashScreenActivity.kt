package com.example.maharlika_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.maharlika_app.R
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.user.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreenActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({

          checkUser()


        },2000)
    }
    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null){
            //user not login
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{

            val dbref = FirebaseDatabase.getInstance().getReference("Users")
            dbref.child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userType = snapshot.child("userType").value

                        if (userType == "member") {
                            Toast.makeText(this@SplashScreenActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
                            finish()

                        } else if (userType == "admin") {
                            Toast.makeText(this@SplashScreenActivity, "Welcome Admin", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SplashScreenActivity,AdminHolderActivity::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }
    }
}