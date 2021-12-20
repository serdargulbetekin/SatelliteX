package com.example.satellitex.satellitelist.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.config.StringProvider
import com.example.config.optMessage
import com.example.constants.Constants
import com.example.satellitex.R
import com.example.satellitex.room.Satellite
import com.example.satellitex.satellitelist.domain.SatelliteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SatelliteListViewModel @Inject constructor(
    private val satelliteRepository: SatelliteRepository,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _satelliteList = MutableLiveData<List<Satellite>>()
    val satelliteList: LiveData<List<Satellite>>
        get() = _satelliteList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        loadData()
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        _progress.postValue(Constants.PROGRESS_VISIBLE)
        satelliteRepository.loadData()
            .subscribe({
                _progress.postValue(Constants.PROGRESS_GONE)
                if (it.isNotEmpty()) {
                    _satelliteList.postValue(it)
                } else {
                    _errorMessage.postValue(stringProvider.getString(R.string.empty_list_message))
                }
            }, {
                _progress.postValue(Constants.PROGRESS_GONE)
                _errorMessage.postValue(it.optMessage(stringProvider))
            })
    }

}
