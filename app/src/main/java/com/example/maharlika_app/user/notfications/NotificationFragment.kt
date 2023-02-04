package com.example.maharlika_app.user.notfications

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.R
import com.example.maharlika_app.databinding.FragmentEventUserBinding
import com.example.maharlika_app.databinding.FragmentNotificationBinding
import com.example.maharlika_app.user.events.UserEventAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    // array list to hold events
    private lateinit var notifArrayList : ArrayList<ModelNotif>

    //adapter
    private lateinit var adapter : NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this@NotificationFragment.requireContext())
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)
        getEvents()
    }
    private fun getEvents() {
        //initialize
        notifArrayList = ArrayList()

        val dbRef = FirebaseDatabase.getInstance().getReference("Notifications")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list
                notifArrayList.clear()
                for (data in snapshot.children) {
                    //data as model
                    val model = data.getValue(ModelNotif::class.java)

                    // add to array
                    notifArrayList.add(model!!)
                }
                //set up adapter
                lifecycleScope.launchWhenResumed {
                    adapter = NotificationAdapter(this@NotificationFragment.requireContext(), notifArrayList)
                    //set to recycler
                    binding.notifRv.setHasFixedSize(true)
                    val layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.notifRv.layoutManager = layoutManager
                    binding.notifRv.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}