package com.example.satellitex.satellitedetail.domain.interactor

import android.content.Context
import android.util.Log
import com.example.config.disposeIfNotDisposed
import com.example.satellitex.room.Satellite
import com.example.satellitex.satellitedetail.data.SatellitePosition
import com.example.satellitex.satellitedetail.data.SatellitePositionList
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GenerateSatellitePositionData @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val satellitePositionList = mutableListOf<SatellitePositionList>()

    fun getPositionData(id: Int) =
        Observable.create<SatellitePositionList> { emitter ->
            populateData().firstOrNull { it.id == id.toString() }?.let {
                emitter.onNext(it)
                Log.d("PositionLog", it.id)
            }
        }

    private fun populateData() = if (satellitePositionList.isEmpty()) {
        generatePositions(loadDPositionsJsonFromFile()).also {
            satellitePositionList.addAll(it)
        }
    } else {
        satellitePositionList
    }

    private fun loadDPositionsJsonFromFile(): String {
        try {
            val inputStream: InputStream = context.assets.open("positions.json")
            val inputString = inputStream.bufferedReader().use { it.readText() }
            Log.d(TAG, inputString)
            return inputString
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return ""
    }

    private fun generatePositions(inputString: String): List<SatellitePositionList> {
        val satellitePositionList = mutableListOf<SatellitePositionList>()
        val jsonObject = JSONObject(inputString)
        val listArray = jsonObject.optJSONArray("list")
        listArray?.let { list ->
            for (i in 0 until list.length()) {
                val positionList = mutableListOf<SatellitePosition>()
                val listObject = list.getJSONObject(i)
                val id = listObject.optString("id")
                val positionArray = listObject.optJSONArray("positions")
                positionArray?.let { posArray ->
                    for (j in 0 until posArray.length()) {
                        val posObject = posArray.getJSONObject(j)
                        positionList.add(
                            SatellitePosition(
                                posX = posObject.optDouble("posX"),
                                posY = posObject.optDouble("posY"),
                            )
                        )
                    }
                }

                satellitePositionList.add(
                    SatellitePositionList(
                        id = id,
                        satellitePosition = positionList
                    )
                )
            }
        }
        return satellitePositionList
    }

    private companion object {
        private const val TAG = "LOAD_JSON_POSITION"
        private const val INITIAL_DELAY = 0L
        private const val SECOND_INTERVAL = 3L
    }
}