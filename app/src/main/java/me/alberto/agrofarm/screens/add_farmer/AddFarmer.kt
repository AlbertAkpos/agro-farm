package me.alberto.agrofarm.screens.add_farmer

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_add_farmer.*
import me.alberto.agrofarm.databinding.FragmentAddFarmerBinding
import me.alberto.agrofarm.screens.map.MapActivity
import me.alberto.agrofarm.viewmodel.MainViewModel

const val RC_MAP = 2

class AddFarmer : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentAddFarmerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

        viewModel = requireActivity().run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.activity

        binding.addFarm.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, RC_MAP)
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MAP && resultCode == Activity.RESULT_OK) {
            val farmAddress = data?.getBundleExtra("farm_details")?.getString("address")
            val farmCoordinates = data?.getBundleExtra("farm_details")?.getSerializable("coordinates") as List<LatLng>
            val farmLocation = data.getBundleExtra("farm_details")?.getParcelable("location") as Location?
            viewModel.setFarmAddressAndCoordinates(farmAddress, farmCoordinates, farmLocation)
            location_container.visibility = View.VISIBLE
        }
    }
}
