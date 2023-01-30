package com.example.maharlika_app.user.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maharlika_app.R
import com.example.maharlika_app.databinding.FragmentEventUserBinding


class EventUserFragment : Fragment() {

    private lateinit var binding : FragmentEventUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventUserBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}