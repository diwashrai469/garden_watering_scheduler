package com.app.gradenwateringscheduler.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gradenwateringscheduler.databinding.FragmentSettingsViewBinding


class SettingsViewFragment : Fragment() {

    private lateinit var binding: FragmentSettingsViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsViewBinding.inflate(inflater, container, false)

        return binding.root
    }


}