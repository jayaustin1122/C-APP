package com.example.maharlika_app.admin.news

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.databinding.ActivityEditNewsBinding
import com.google.firebase.database.FirebaseDatabase

class EditNewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditNewsBinding
    private var newstId = ""
    private var newsTitle = ""
    private var newsDesc = ""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //get id to edit events
        newstId = intent.getStringExtra("id")!!
        newsTitle = intent.getStringExtra("newsTitle")!!
        newsDesc = intent.getStringExtra("newsDescription")!!
        //set text
        binding.etTitleEventsEdit.setText(newsTitle)
        binding.etDesciptionEventsEdit.setText(newsDesc)


        binding.btnUpdate.setOnClickListener {
            updateData()

        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun updateData() {
        //get data
        newsTitle = binding.etTitleEventsEdit.text.toString()
        newsDesc = binding.etDesciptionEventsEdit.text.toString()

        if (newsTitle.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
        }
        else if (newsDesc.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        }
        else{
            updateNewData()
        }


    }
    private fun updateNewData() {
        progressDialog.setMessage("Updating Event")
        progressDialog.show()

        //set up to db
        val hashMap = HashMap<String, Any?>()

        hashMap["newsTitle"] = "$newsTitle"
        hashMap["newsDescription"] = "$newsDesc"

        val dbRef = FirebaseDatabase.getInstance().getReference("news")
        dbRef.child(newstId)
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