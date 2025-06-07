package com.app.gradenwateringscheduler.ui.home.fragment

import AppDatabase
import PlantsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gradenwateringscheduler.databinding.FragmentHomeViewBinding
import kotlinx.coroutines.launch

class HomeViewFragment : Fragment() {

    private var _binding: FragmentHomeViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeViewBinding.inflate(inflater, container, false)

        // Initialize your database instance
        appDatabase = AppDatabase.getDatabase(requireContext())

        setupClickListeners()
        observePlants()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.addPlant.setOnClickListener {
            val direction = HomeViewFragmentDirections.actionNavHomeToAddPlantViewFragment()
            findNavController().navigate(direction)
        }

        binding.fabAddPlant.setOnClickListener {
            val direction = HomeViewFragmentDirections.actionNavHomeToAddPlantViewFragment()
            findNavController().navigate(direction)
        }
    }

    private fun observePlants() {
        lifecycleScope.launch {
            // Collect the Flow of plants continuously
            appDatabase.plantDao().getAllPlants().collect { plants ->

                if (plants.isEmpty()) {
                    // Show default empty state UI
                    binding.emptyStateLayout.visibility = View.VISIBLE
                    binding.plantsRecyclerView.visibility = View.GONE
                } else {
                    // Show plants list UI
                    binding.emptyStateLayout.visibility = View.GONE
                    binding.plantsRecyclerView.visibility = View.VISIBLE

                    // Setup RecyclerView
                    binding.plantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.plantsRecyclerView.adapter = PlantsAdapter(plants)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
