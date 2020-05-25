package me.alberto.agrofarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Farmer::class, Farm::class], version = 1)
@TypeConverters(Converter::class)
abstract class FarmDatabase : RoomDatabase() {
    abstract val farmerDao: FarmDao

    companion object {
        private lateinit var INSTANCE: FarmDatabase
        fun getFarmDatabase(context: Context): FarmDatabase {
            synchronized(FarmDatabase::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        FarmDatabase::class.java,
                        "farm-databse"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}