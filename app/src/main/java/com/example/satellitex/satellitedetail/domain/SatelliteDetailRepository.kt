package com.example.satellitex.satellitedetail.domain

import com.example.satellitex.satellitedetail.data.SatelliteDetail
import com.example.satellitex.satellitedetail.data.SatellitePositionList
import com.example.satellitex.satellitedetail.domain.interactor.GenerateSatelliteDetailData
import com.example.satellitex.satellitedetail.domain.interactor.GenerateSatellitePositionData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SatelliteDetailRepository @Inject constructor(
    private val generateSatelliteDetailData: GenerateSatelliteDetailData,
    private val generateSatellitePositionData: GenerateSatellitePositionData,
) {

    fun loadData(id: Int): Single<Pair<SatelliteDetail?, SatellitePositionList?>> {
        return Singles.zip(
            generateSatelliteDetailData.getDetailData(id),
            generateSatellitePositionData.getPositionData(id)
        ) { detailData, positionData ->
            detailData to positionData
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private const val TAG = "LOAD_JSON_DETAIL_REPO"
    }
}