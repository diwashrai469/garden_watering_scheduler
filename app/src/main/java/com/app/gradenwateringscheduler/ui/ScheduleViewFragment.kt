package com.app.gradenwateringscheduler.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gradenwateringscheduler.R
import com.app.gradenwateringscheduler.databinding.FragmentScheduleViewBinding
import com.app.gradenwateringscheduler.databinding.FragmentSettingsViewBinding

class ScheduleViewFragment : Fragment() {
    private lateinit var binding: FragmentScheduleViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleViewBinding.inflate(inflater, container, false)

        return binding.root
    }


}