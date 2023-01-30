package com.example.maharlika_app.admin.manageAcc

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maharlika_app.R
import com.example.maharlika_app.admin.news.AddNewsActivity
import com.example.maharlika_app.admin.news.ModelNews
import com.example.maharlika_app.admin.news.NewsAdapter
import com.example.maharlika_app.databinding.FragmentManageAccountsBinding
import com.example.maharlika_app.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageAccountsFragment : Fragment() {

    private lateinit var binding : FragmentManageAccountsBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    // array list to hold events
    private lateinit var accArrayList : ArrayList<UserModel>

    //adapter
    private lateinit var adapter : AccountsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageAccountsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this@ManageAccountsFragment.requireContext())
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)
        getEvents()


    }
    private fun getEvents() {
        //initialize
        accArrayList = ArrayList()

        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list
                accArrayList.clear()
                for (data in snapshot.children){
                    //data as model
                    val model = data.getValue(UserModel::class.java)

                    // add to array
                    accArrayList.add(model!!)
                }
                //set up adapter
                adapter = AccountsAdapter(this@ManageAccountsFragment.requireContext(),accArrayList)
                //set to recycler
                binding.adminEventRv.setHasFixedSize(true)
                binding.adminEventRv.layoutManager = LinearLayoutManager(context)
                binding.adminEventRv.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}