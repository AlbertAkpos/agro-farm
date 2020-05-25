package me.alberto.agrofarm.database

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "farmer_table")
data class Farmer(
    @PrimaryKey(autoGenerate = true)
    val farmerId: Long = 0L,
    val name: String,
    val age: Int,
    val address: String
)

@Entity(
    tableName = "farm_table", foreignKeys = [ForeignKey(
        entity = Farmer::class, parentColumns = ["farmerId"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Farm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    @Embedded
    val location: FarmLocation,
    val coordinates: List<FarmLocation>,
    val farmerOwnerId: Long
)

data class FarmLocation(
    var lat: Double,
    var long: Double
){
    constructor(): this(0.0, 0.0)
}


data class FarmerWithFarms(
    @Embedded val farmer: Farmer,
    @Relation(
        parentColumn = "farmerId",
        entityColumn = "farmerOwnerId"
    )
    val farms: List<Farm>
)


class Converter {
    @TypeConverter
    fun fromFarmLocationList(coordinates: List<FarmLocation>): String {
        val gson = Gson()
        val type = object : TypeToken<List<FarmLocation>>(){}.type
        return gson.toJson(coordinates, type)
    }

    @TypeConverter
    fun toFarmLocationList(string: String): List<FarmLocation> {
        val gson = Gson()
        val type = object : TypeToken<List<FarmLocation>>(){}.type
        return gson.fromJson(string, type)
    }
}