package me.alberto.agrofarm.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "farmer_table")
@Parcelize
data class Farmer(
    @PrimaryKey(autoGenerate = true)
    val farmerId: Long = 0L,
    val name: String,
    val age: Int,
    val image: String
): Parcelable

@Entity(
    tableName = "farm_table", foreignKeys = [ForeignKey(
        entity = Farmer::class, parentColumns = ["farmerId"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Farm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    @Embedded
    val location: @RawValue FarmLocation,
    val coordinates: List<LatLng>,
    val farmerOwnerId: Long
): Parcelable

@Parcelize
data class FarmLocation(
    var lat: Double,
    var long: Double
): Parcelable{
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
    fun fromFarmLocationList(coordinates: List<LatLng>): String {
        val gson = Gson()
        val type = object : TypeToken<List<LatLng>>(){}.type
        return gson.toJson(coordinates, type)
    }

    @TypeConverter
    fun toFarmLocationList(string: String): List<LatLng> {
        val gson = Gson()
        val type = object : TypeToken<List<LatLng>>(){}.type
        return gson.fromJson(string, type)
    }

}