package com.example.satellitex.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Satellite::class)], version = 1)
abstract class SatelliteDatabase : RoomDatabase() {
    abstract fun satelliteDao(): SatelliteDao
}