package com.example.maharlika_app.admin.events

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
import com.example.maharlika_app.databinding.FragmentEventAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EventAdminFragment : Fragment() {

    private lateinit var binding: FragmentEventAdminBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    // array list to hold events
    private lateinit var eventArrayList : ArrayList<ModelEvent>

    //adapter
    private lateinit var adapter : EventAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventAdminBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this@EventAdminFragment.requireContext())
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)
        getEvents()

        binding.btnAddEvent.setOnClickListener {
            startActivity(Intent(this@EventAdminFragment.requireContext(), AddEventActivity::class.java))
        }
    }
    private fun getEvents() {
        //initialize
        eventArrayList = ArrayList()

        val dbRef = FirebaseDatabase.getInstance().getReference("events")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list
                eventArrayList.clear()
                for (data in snapshot.children){
                    //data as model
                    val model = data.getValue(ModelEvent::class.java)

                    // add to array
                    eventArrayList.add(model!!)
                }
                //set up adapter
                adapter = EventAdapter(this@EventAdminFragment.requireContext(),eventArrayList)
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