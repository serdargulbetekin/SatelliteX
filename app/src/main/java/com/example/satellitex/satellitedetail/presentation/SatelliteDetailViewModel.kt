package com.example.satellitex.satellitedetail.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.config.StringProvider
import com.example.config.optMessage
import com.example.constants.Constants
import com.example.constants.Constants.COMMA
import com.example.constants.Constants.DIVIDER
import com.example.constants.Constants.LEFT_PARENTHESIS
import com.example.constants.Constants.RIGHT_PARENTHESIS
import com.example.constants.Constants.SPACE
import com.example.satellitex.R
import com.example.satellitex.room.Satellite
import com.example.satellitex.satellitedetail.data.SatelliteDetail
import com.example.satellitex.satellitedetail.data.SatellitePositionList
import com.example.satellitex.satellitedetail.domain.SatelliteDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val satelliteDetailRepository: SatelliteDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val intentDataSatelliteId =
        savedStateHandle.get<Int>(SatelliteDetailActivity.EXTRAS_SATELLITE_ID)

    private val intentDataSatelliteName =
        savedStateHandle.get<String>(SatelliteDetailActivity.EXTRAS_SATELLITE_NAME)

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val nameMutableLiveData = MutableLiveData<String?>()
    val nameLiveData: LiveData<String?>
        get() = nameMutableLiveData

    private val dateMutableLiveData = MutableLiveData<String?>()
    val dateLiveData: LiveData<String?>
        get() = dateMutableLiveData

    private val heightMassMutableLiveData = MutableLiveData<String>()
    val heightMassLiveData: LiveData<String>
        get() = heightMassMutableLiveData

    private val costMutableLiveData = MutableLiveData<String>()
    val costLiveData: LiveData<String>
        get() = costMutableLiveData

    private val positionMutableLiveData = MutableLiveData<String>()
    val positionLiveData: LiveData<String>
        get() = positionMutableLiveData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        intentDataSatelliteId?.let { satelliteId ->
            loadUIData(satelliteId)
        } ?: _errorMessage.postValue(stringProvider.getString(R.string.id_not_found))
    }

    @SuppressLint("CheckResult")
    fun loadUIData(satelliteId: Int) {
        _progress.postValue(Constants.PROGRESS_VISIBLE)
        satelliteDetailRepository.loadData(satelliteId).map {
            val satellitePosition = it.second?.satellitePosition?.getOrNull(0)
            val heightMass =
                stringProvider.getStringWithArgs(
                    R.string.height_mass,
                    it.first?.height.toString() + DIVIDER + it.first?.mass.toString()
                )
            val cost =
                stringProvider.getStringWithArgs(R.string.cost, it.first?.costPerLaunch.toString())
            val lastPosition =
                stringProvider.getStringWithArgs(
                    R.string.last_position,
                    LEFT_PARENTHESIS + satellitePosition?.posX.toString() + COMMA + SPACE + satellitePosition?.posY.toString() + RIGHT_PARENTHESIS
                )
            SatelliteDetailVMItem(it.first?.firstFlight, heightMass, cost, lastPosition)
        }.subscribe({ satelliteDetailVMItem ->
            _progress.postValue(Constants.PROGRESS_GONE)
            nameMutableLiveData.postValue(intentDataSatelliteName)
            dateMutableLiveData.postValue(satelliteDetailVMItem.date)
            heightMassMutableLiveData.postValue(satelliteDetailVMItem.heightMass)
            costMutableLiveData.postValue(satelliteDetailVMItem.cost)
            positionMutableLiveData.postValue(satelliteDetailVMItem.lastPosition)
        }, {
            _progress.postValue(Constants.PROGRESS_GONE)
            _errorMessage.postValue(it.optMessage(stringProvider))
        })
    }
}

data class SatelliteDetailVMItem(
    val date: String?,
    val heightMass: String,
    val cost: String,
    val lastPosition: String
)

