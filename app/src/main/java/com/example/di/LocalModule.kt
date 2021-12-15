package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.satellitex.room.SatelliteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SatelliteDatabase {
        return Room.databaseBuilder(
            context,
            SatelliteDatabase::class.java,
            "satellite.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSatelliteDao(db: SatelliteDatabase) = db.satelliteDao()


}
