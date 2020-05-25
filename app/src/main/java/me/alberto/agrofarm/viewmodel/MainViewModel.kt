package me.alberto.agrofarm.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import me.alberto.agrofarm.database.Farm
import me.alberto.agrofarm.database.FarmLocation
import me.alberto.agrofarm.database.Farmer
import me.alberto.agrofarm.database.FarmerWithFarms
import me.alberto.agrofarm.repository.FarmRepository

class MainViewModel(private val repository: FarmRepository) : ViewModel() {

    val farmers = repository.getFarmers()
    val farms = repository.getFarms()

    var farmerName = MutableLiveData<String>()
    var farmerAge = MutableLiveData<String>()
    var farmName = MutableLiveData<String>()

    private val farmerObserver = Observer<List<Farmer>> { observeFarmer(it) }

    private var chechFarmers: List<Farmer>? = null

    private val _farmerWithFarms = MutableLiveData<FarmerWithFarms>()
    val farmerWithFarms = repository.getFarmerWithFarms(1)



    var farmAddress: String? = null
        private set
    private var farmCoordinates: List<LatLng>? = null
    private var farmLocation: Location? = null


    init {
        farmers.observeForever(farmerObserver)
    }

    private fun observeFarmer(listOfFarmers: List<Farmer>?) {
        listOfFarmers ?: return
        if (listOfFarmers.isEmpty()  || chechFarmers.isNullOrEmpty()) return


        if (listOfFarmers != chechFarmers) {
            val farmOwnerId = listOfFarmers[listOfFarmers.size - 1].farmerId
            val farmLatLngLocation = FarmLocation(farmLocation!!.latitude, farmLocation!!.longitude)
            val farm = Farm(
                name = farmName.value!!,
                location = farmLatLngLocation,
                coordinates = farmCoordinates!!,
                farmerOwnerId = farmOwnerId
            )
            println(
                """
            farmers: ${farmers.value}
            about to save farm
        """
            )
            addFarm(farm)
        }
    }

    fun setFarmAddressAndCoordinates(
        address: String?,
        coordinates: List<LatLng>?,
        location: Location?
    ) {
        if (address == null || coordinates == null) {
            return
        }
        farmAddress = address
        farmCoordinates = coordinates
        farmLocation = location
    }

    fun onAddFarmer() {

        println("""
           onAddFarmer called 
            
        """)

        if (farmAddress.isNullOrEmpty()
            || farmCoordinates.isNullOrEmpty()
            || farmerName.value.isNullOrEmpty()
            || farmerAge.value.isNullOrEmpty()
            || farmName.value.isNullOrEmpty()
        ) {
            return
        }
        chechFarmers = farmers.value
        val farmer = Farmer(name = farmerName.value!!, age = farmerAge.value!!.toInt())

        println("""
            
           about to save farmer 
        """)

        addFarmer(farmer)
    }


    private fun addFarmer(farmer: Farmer) {
        viewModelScope.launch {
            repository.addFarmer(farmer)
        }
    }

    fun deleteFarmer(farmer: Farmer) {
        viewModelScope.launch {
            repository.deleteFarmer(farmer)
        }
    }


    fun addFarm(farm: Farm) {
        viewModelScope.launch {
            repository.addFarm(farm)
        }
    }


    override fun onCleared() {
        super.onCleared()
        farmers.removeObserver(farmerObserver)
    }

}