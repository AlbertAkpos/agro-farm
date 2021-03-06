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
    var farmerImageUrl = MutableLiveData<String>()
        private set
    private val _farmAddress = MutableLiveData<String>()
    val farmAddress: LiveData<String>
        get() = _farmAddress


    private var farmCoordinates: List<LatLng>? = null
    private var farmLocation: Location? = null

    private val farmerObserver = Observer<List<Farmer>> { observeFarmer(it) }

    private var chechFarmersList: List<Farmer>? = null

    private val _farmerWithFarms = MutableLiveData<FarmerWithFarms>()
    val farmerWithFarms: LiveData<FarmerWithFarms>
        get() = _farmerWithFarms


    private val _navigateToDashboard = MutableLiveData<Boolean>()
    val navigateToDashboard: LiveData<Boolean>
        get() = _navigateToDashboard


    init {
        farmers.observeForever(farmerObserver)
    }

    private fun observeFarmer(listOfFarmers: List<Farmer>?) {


        if (listOfFarmers.isNullOrEmpty() || farmLocation == null) return


        if (listOfFarmers != chechFarmersList) {

            val farmOwnerId = listOfFarmers[listOfFarmers.size - 1].farmerId
            val farmLatLngLocation = FarmLocation(farmLocation!!.latitude, farmLocation!!.longitude)
            val farm = Farm(
                name = farmName.value!!,
                location = farmLatLngLocation,
                coordinates = farmCoordinates!!,
                farmerOwnerId = farmOwnerId
            )

            addFarm(farm)
            _navigateToDashboard.value = true
            clearFields()
        }
    }

    fun getFarmerWithFarm(id: Long) {
        val farmerFarm = repository.getFarmerWithFarms(id)
        val farmerFarmObserver = Observer<FarmerWithFarms> { observeFarmerFarm(it) }
        farmerFarm.observeForever(farmerFarmObserver)
    }

    fun observeFarmerFarm(farmer: FarmerWithFarms) {
        _farmerWithFarms.value = farmer
    }

    private fun clearFields() {
        farmerName.value = null
        farmerAge.value = null
        farmName.value = null
        farmerImageUrl.value = null
        _farmAddress.value = null
        farmCoordinates = null
        farmLocation = null
    }


    fun navigateToDashboardDone() {
        _navigateToDashboard.value = null
    }

    fun setFarmAddressAndCoordinates(
        address: String?,
        coordinates: List<LatLng>?,
        location: Location?
    ) {
        if (address == null || coordinates == null) {
            return
        }

        _farmAddress.value = address

        farmCoordinates = coordinates
        farmLocation = location
    }

    fun setFarmerImage(string: String) {
        farmerImageUrl.value = string
    }

    fun onAddFarmer() {

        if (_farmAddress.value.isNullOrEmpty()
            || farmCoordinates.isNullOrEmpty()
            || farmerName.value.isNullOrEmpty()
            || farmerAge.value.isNullOrEmpty()
            || farmName.value.isNullOrEmpty()
            || farmerImageUrl.value == null
        ) {
            return
        }
        chechFarmersList = farmers.value
        val farmer = Farmer(
            name = farmerName.value!!,
            age = farmerAge.value!!.toInt(),
            image = farmerImageUrl.value!!
        )

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

    fun resetFarmerWithFarm() {
        _farmerWithFarms.value = null
    }

}