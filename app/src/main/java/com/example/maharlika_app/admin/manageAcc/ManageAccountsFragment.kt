package com.example.maharlika_app.admin.manageAcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maharlika_app.R
import com.example.maharlika_app.databinding.FragmentManageAccountsBinding

class ManageAccountsFragment : Fragment() {

    private lateinit var binding : FragmentManageAccountsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageAccountsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
}