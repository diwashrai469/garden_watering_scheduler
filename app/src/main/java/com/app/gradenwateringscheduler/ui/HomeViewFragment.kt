package com.app.gradenwateringscheduler.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gradenwateringscheduler.R
import com.app.gradenwateringscheduler.databinding.FragmentHomeViewBinding


class HomeViewFragment : Fragment() {

    private lateinit var binding: FragmentHomeViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeViewBinding.inflate(inflater, container, false)
        return binding.root
    }


}