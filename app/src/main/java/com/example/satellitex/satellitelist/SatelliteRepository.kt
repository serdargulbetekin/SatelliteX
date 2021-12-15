package com.example.satellitex.satellitelist

import android.content.Context
import android.util.Log
import com.example.satellitex.room.Satellite
import com.example.satellitex.room.SatelliteDao
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.io.InputStream
import javax.inject.Inject

class SatelliteRepository @Inject constructor(
    private val satelliteDao: SatelliteDao,
    @ApplicationContext private val context: Context
) {

    fun loadData(): Observable<List<Satellite>> {
        return Observable.fromCallable { satelliteDao.getAll() }.concatMap { dbSatelliteList ->
            if (dbSatelliteList.isEmpty()) {
                Observable.fromCallable { populateData() }.concatMap { insertList ->
                    satelliteDao.insertAll(insertList)
                    Observable.just(insertList)
                }
            } else {
                Observable.just(dbSatelliteList)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadJsonFromFile(): String {
        try {
            val inputStream: InputStream = context.assets.open("satellite-list.json")
            val inputString = inputStream.bufferedReader().use { it.readText() }
            Log.d(TAG, inputString)
            return inputString
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return ""
    }

    private fun generateSatelliteList(inputString: String): List<Satellite> {
        val satelliteList = mutableListOf<Satellite>()
        val jsonArray = JSONArray(inputString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            satelliteList.add(
                Satellite(
                    id = jsonObject.optInt("id"),
                    active = jsonObject.optBoolean("active"),
                    name = jsonObject.optString("name")
                )
            )
        }
        return satelliteList
    }

    private fun populateData() = generateSatelliteList(loadJsonFromFile())

    companion object {
        private const val TAG = "LOAD JSON"
    }
}