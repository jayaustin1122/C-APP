package com.example.maharlika_app.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.maharlika_app.MainActivity
import com.example.maharlika_app.R
import com.example.maharlika_app.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("CustomSplashScreen")
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
            dbref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userType = snapshot.child("userType").value

                        if (userType == "member") {
                            Toast.makeText(this@SplashScreenActivity, "Login Successfully", Toast.LENGTH_SHORT).show()


                        } else if (userType == "admin") {
                            Toast.makeText(this@SplashScreenActivity, "Welcome Admin", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }
    }
}