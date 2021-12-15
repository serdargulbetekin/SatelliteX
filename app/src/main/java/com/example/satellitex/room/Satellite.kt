package com.example.satellitex.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellite")
data class Satellite( @PrimaryKey
                 @ColumnInfo(name = "id")
                 val id: Int,
                 @ColumnInfo(name = "active")
                 val active: Boolean,
                 @ColumnInfo(name = "name")
                 val name: String)
