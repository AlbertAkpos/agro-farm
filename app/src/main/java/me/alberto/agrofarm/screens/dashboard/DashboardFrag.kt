package me.alberto.agrofarm.screens.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import me.alberto.agrofarm.databinding.DashboardFragBinding
import me.alberto.agrofarm.viewmodel.MainViewModel

class DashboardFrag : Fragment() {
    private lateinit var binding: DashboardFragBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DashboardFragBinding.inflate(inflater, container, false)
        init()

        binding.addFarmerBtn.setOnClickListener{
            this.findNavController().navigate(DashboardFragDirections.actionDashboardToAddFarmer())
        }

        return binding.root
    }


    private fun init(){
        viewModel = requireActivity().run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}
