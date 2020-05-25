package me.alberto.agrofarm.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FarmDao {
    @Query("SELECT * from farmer_table")
    fun getFarmers(): LiveData<List<Farmer>>

    @Insert
    suspend fun addFarmer(farmer: Farmer)

    @Delete
    suspend fun deleteFarmer(farmer: Farmer)

    @Insert
    suspend fun addFarm(farm: Farm)

    @Transaction
    @Query("SELECT * from farmer_table WHERE farmerId=:id")
    fun getFarmersWithFarm(id: Long): LiveData<FarmerWithFarms>

    @Query("SELECT * from farm_table")
    fun getFarms(): LiveData<List<Farm>>

}