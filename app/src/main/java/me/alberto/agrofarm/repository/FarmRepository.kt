package me.alberto.agrofarm.repository

import me.alberto.agrofarm.database.Farm
import me.alberto.agrofarm.database.FarmDatabase
import me.alberto.agrofarm.database.Farmer

class FarmRepository(private val farmDatabase: FarmDatabase) {
    fun getFarmers() = farmDatabase.farmerDao.getFarmers()
    suspend fun addFarmer(farmer: Farmer) = farmDatabase.farmerDao.addFarmer(farmer)
    suspend fun deleteFarmer(farmer: Farmer) = farmDatabase.farmerDao.deleteFarmer(farmer)
    suspend fun addFarm(farm: Farm) = farmDatabase.farmerDao.addFarm(farm)
    fun getFarmerWithFarms(farmerId: Long) = farmDatabase.farmerDao.getFarmersWithFarm(farmerId)
    fun getFarms() = farmDatabase.farmerDao.getFarms()
}