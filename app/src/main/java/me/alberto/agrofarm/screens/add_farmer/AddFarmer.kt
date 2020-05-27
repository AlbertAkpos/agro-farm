package me.alberto.agrofarm.screens.add_farmer

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_farmer.*
import me.alberto.agrofarm.R
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
        binding.lifecycleOwner = viewLifecycleOwner


        setClickListeners()
        observe()
        return binding.root
    }

    private fun setClickListeners() {
        binding.addFarm.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, RC_MAP)
        }

        binding.addProfileImg.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1)
                .start(requireContext(), this)
        }
    }

    private fun observe() {
        viewModel.navigateToDashboard.observe(viewLifecycleOwner, Observer { navigate ->
            navigate ?: return@Observer
            if (navigate) {
                this.findNavController().navigate(AddFarmerDirections.actionAddFarmerToDashboard())
                viewModel.navigateToDashboardDone()
            }
        })

        viewModel.farmerImageUrl.observe(viewLifecycleOwner, Observer { imageUrl ->
            imageUrl ?: return@Observer
            Glide.with(requireContext())
                .load(imageUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                )
                .into(binding.profileImg)

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MAP && resultCode == Activity.RESULT_OK) {
            val farmAddress = data?.getBundleExtra("farm_details")?.getString("address")
            val farmCoordinates =
                data?.getBundleExtra("farm_details")?.getSerializable("coordinates") as List<LatLng>
            val farmLocation =
                data.getBundleExtra("farm_details")?.getParcelable("location") as Location?
            viewModel.setFarmAddressAndCoordinates(farmAddress, farmCoordinates, farmLocation)
            location_container.visibility = View.VISIBLE
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val imageUrl = CropImage.getActivityResult(data).uri.toString()
            viewModel.setFarmerImage(imageUrl)
        }
    }
}
