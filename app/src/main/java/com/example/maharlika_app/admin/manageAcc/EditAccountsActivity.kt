package com.example.maharlika_app.admin.manageAcc

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityEditAccountsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditAccountsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditAccountsBinding
    private var acctId = ""
    private var fullname = ""
    private var pass = ""
    private var image = ""
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //get id to edit events
        acctId = intent.getStringExtra("id")!!
        fullname = intent.getStringExtra("fullname")!!
        pass = intent.getStringExtra("pass")!!
        image = intent.getStringExtra("image")!!

        binding.etTitleEventsEdit.setText(fullname)
        binding.etDesciptionEventsEdit.setText(pass)

        Glide.with(this)
            .load(image)
            .into(binding.imageView)
        binding.btnUpdate.setOnClickListener {
            updateData()

        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }


    }
    private fun updateData() {
        //get data
        fullname = binding.etTitleEventsEdit.text.toString()
        pass = binding.etDesciptionEventsEdit.text.toString()

        if (fullname.isEmpty()) {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else if (pass.isEmpty()) {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
        else{
            updateNewData()
        }


    }
    private var fullnames = ""
    private var passs = ""
    private fun updateNewData() {
        progressDialog.setMessage("Updating Account")
        progressDialog.show()

        //set up to db
        val hashMap = HashMap<String, Any?>()

        hashMap["fullName"] = "$fullnames"
        hashMap["password"] = "$passs"
        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.child(acctId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "News Updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminHolderActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Unable to update due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }


    }
}