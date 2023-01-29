package com.example.maharlika_app.admin.events

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maharlika_app.R
import com.example.maharlika_app.adapter.AdminEventAdapter
import com.example.maharlika_app.auth.LoginActivity
import com.example.maharlika_app.databinding.FragmentEventAdminBinding
import com.example.maharlika_app.model.EventModel
import com.google.firebase.auth.FirebaseAuth


class EventAdminFragment : Fragment() {

    private lateinit var binding: FragmentEventAdminBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    // array list to hold events
    private lateinit var eventArrayList : ArrayList<EventModel>

    //adapter
    private lateinit var adapter : AdminEventAdapter
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

        binding.btnAddEvent.setOnClickListener {
            startActivity(Intent(this@EventAdminFragment.requireContext(), AddEventActivity::class.java))
        }
    }

}