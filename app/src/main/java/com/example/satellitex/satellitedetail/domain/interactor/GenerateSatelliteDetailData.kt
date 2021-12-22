package com.example.satellitex.satellitedetail.domain.interactor

import android.content.Context
import android.util.Log
import com.example.satellitex.satellitedetail.data.SatelliteDetail
import com.example.satellitex.satellitedetail.data.SatellitePositionList
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONArray
import java.io.InputStream
import javax.inject.Inject

class GenerateSatelliteDetailData @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val satelliteDetailList = mutableListOf<SatelliteDetail>()

    fun getDetailData(id: Int) =
        Observable.fromCallable { populateData().firstOrNull { it.id == id } }

    private fun populateData() = if (satelliteDetailList.isEmpty()) {
        generateSatelliteList(loadDetailJsonFromFile()).also {
            satelliteDetailList.addAll(it)
        }
    } else {
        satelliteDetailList
    }

    private fun loadDetailJsonFromFile(): String {
        try {
            val inputStream: InputStream = context.assets.open("satellite-detail.json")
            val inputString = inputStream.bufferedReader().use { it.readText() }
            Log.d(TAG, inputString)
            return inputString
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return ""
    }

    private fun generateSatelliteList(inputString: String): List<SatelliteDetail> {
        val satelliteDetailList = mutableListOf<SatelliteDetail>()
        val jsonArray = JSONArray(inputString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            satelliteDetailList.add(
                SatelliteDetail(
                    id = jsonObject.optInt("id"),
                    costPerLaunch = jsonObject.optInt("cost_per_launch"),
                    firstFlight = jsonObject.optString("first_flight"),
                    height = jsonObject.optInt("height"),
                    mass = jsonObject.optInt("mass")
                )
            )
        }
        return satelliteDetailList
    }

    companion object {
        private const val TAG = "LOAD_JSON_DETAIL"
    }
}