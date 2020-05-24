package me.alberto.agrofarm.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.alberto.agrofarm.databinding.DashboardFragBinding

class DashboardFrag : Fragment() {
    private lateinit var binding: DashboardFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragBinding.inflate(inflater, container, false)

        binding.addFarmerBtn.setOnClickListener{
            this.findNavController().navigate(DashboardFragDirections.actionDashboardToAddFarmer())
        }

        return binding.root
    }
}
