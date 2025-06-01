package com.app.gradenwateringscheduler.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gradenwateringscheduler.databinding.FragmentHomeViewBinding


class HomeViewFragment : Fragment() {

    private lateinit var binding: FragmentHomeViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeViewBinding.inflate(inflater, container, false)

        binding.addPlant.setOnClickListener {
            val direction =  HomeViewFragmentDirections.actionNavHomeToAddPlantViewFragment()
            findNavController().navigate(direction)
        }

        binding.fabAddPlant.setOnClickListener {
            val direction =  HomeViewFragmentDirections.actionNavHomeToAddPlantViewFragment()
            findNavController().navigate(direction)
        }

        return binding.root
    }
}