package com.app.gradenwateringscheduler.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gradenwateringscheduler.databinding.FragmentAddPlantViewBinding


class AddPlantViewFragment : Fragment() {
    private lateinit var binding: FragmentAddPlantViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlantViewBinding.inflate(inflater, container, false)
        return binding.root
    }
}