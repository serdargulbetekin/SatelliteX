package com.example.satellitex.satellitedetail.domain

import com.example.satellitex.satellitedetail.data.SatelliteDetail
import com.example.satellitex.satellitedetail.data.SatellitePositionList
import com.example.satellitex.satellitedetail.domain.interactor.GenerateSatelliteDetailData
import com.example.satellitex.satellitedetail.domain.interactor.GenerateSatellitePositionData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SatelliteDetailRepository @Inject constructor(
    private val generateSatelliteDetailData: GenerateSatelliteDetailData,
    private val generateSatellitePositionData: GenerateSatellitePositionData,
) {

    fun loadData(id: Int): Observable<Pair<SatelliteDetail?, SatellitePositionList>> {
        return Observable.combineLatest<SatelliteDetail?, SatellitePositionList, Pair<SatelliteDetail?, SatellitePositionList>>(
            generateSatelliteDetailData.getDetailData(id),
            Observable.interval(
                INITIAL_DELAY,
                SECOND_INTERVAL,
                TimeUnit.SECONDS
            ).flatMap { generateSatellitePositionData.getPositionData(id) },
            BiFunction { t1, t2 ->
                return@BiFunction Pair(t1, t2)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    private companion object {
        private const val INITIAL_DELAY = 0L
        private const val SECOND_INTERVAL = 3L
    }
}