package me.alberto.agrofarm.screens.farmer_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.alberto.agrofarm.databinding.FragmentFarmerListBinding
import me.alberto.agrofarm.screens.farmMap.FarmMap
import me.alberto.agrofarm.screens.map.MapActivity
import me.alberto.agrofarm.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class FarmerListFrag : Fragment() {
    private lateinit var binding: FragmentFarmerListBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmerListBinding.inflate(inflater, container, false)

        viewModel = requireActivity().run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }

        binding.farmerList.adapter = FarmerListAdapter(FarmerListAdapter.FarmerClickListener {
            viewModel.getFarmerWithFarm(it.farmerId)
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.activity

        viewModel.farmerWithFarms.observe(viewLifecycleOwner, Observer { farmerWithFarm ->
            farmerWithFarm ?: return@Observer

            val intent = Intent(activity, FarmMap::class.java)
            val farm = farmerWithFarm.farms[0]
            intent.putExtra("farmer", farm)
            startActivity(intent)
            viewModel.resetFarmerWithFarm()
        })


        return binding.root
    }

}
