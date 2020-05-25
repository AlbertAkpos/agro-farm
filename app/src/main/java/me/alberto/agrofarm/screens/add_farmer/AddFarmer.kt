package me.alberto.agrofarm.screens.add_farmer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.alberto.agrofarm.databinding.FragmentAddFarmerBinding
import me.alberto.agrofarm.screens.map.MapActivity

const val RC_MAP = 2

class AddFarmer : Fragment() {

    private lateinit var binding: FragmentAddFarmerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

        binding.addFarm.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, RC_MAP)
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MAP && resultCode == Activity.RESULT_OK) {
            println(
                """
                
                data: ${data?.getSerializableExtra("coordinates").toString()}
                
            """
            )
        }
    }
}
