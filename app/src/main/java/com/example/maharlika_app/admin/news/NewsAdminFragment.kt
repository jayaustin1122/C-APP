package com.example.maharlika_app.admin.news

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maharlika.ui.admin.events.EventAdapter
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.R
import com.example.maharlika_app.admin.events.AddEventActivity
import com.example.maharlika_app.databinding.FragmentNewsAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewsAdminFragment : Fragment() {

    private lateinit var binding : FragmentNewsAdminBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    // array list to hold events
    private lateinit var newsArrayList : ArrayList<ModelNews>

    //adapter
    private lateinit var adapter : NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsAdminBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this@NewsAdminFragment.requireContext())
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)
        getEvents()

        binding.btnAddEvent.setOnClickListener {
            startActivity(Intent(this@NewsAdminFragment.requireContext(), AddNewsActivity::class.java))
        }

    }
    private fun getEvents() {
        //initialize
        newsArrayList = ArrayList()

        val dbRef = FirebaseDatabase.getInstance().getReference("news")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list
                newsArrayList.clear()
                for (data in snapshot.children){
                    //data as model
                    val model = data.getValue(ModelNews::class.java)

                    // add to array
                    newsArrayList.add(model!!)
                }
                //set up adapter
                adapter = NewsAdapter(this@NewsAdminFragment.requireContext(),newsArrayList)
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