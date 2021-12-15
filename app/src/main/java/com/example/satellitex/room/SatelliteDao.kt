package com.example.satellitex.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SatelliteDao {

    @Query("SELECT * FROM satellite")
    fun getAll(): List<Satellite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(satelliteList: List<Satellite>)
}