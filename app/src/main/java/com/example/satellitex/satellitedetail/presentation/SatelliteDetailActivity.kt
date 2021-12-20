package com.example.satellitex.satellitedetail.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.config.setClickableText
import com.example.config.showToast
import com.example.satellitex.databinding.ActivitySatelliteDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class SatelliteDetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<SatelliteDetailViewModel>()

    private val viewBinding by lazy {
        ActivitySatelliteDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.apply {
            viewModel.apply {
                nameLiveData.observe(this@SatelliteDetailActivity,
                    { textViewName.text = it })
                dateLiveData.observe(this@SatelliteDetailActivity,
                    { textViewDate.text = it })
                heightMassLiveData.observe(
                    this@SatelliteDetailActivity,
                    { textViewHeightMass.setClickableText(it) })
                costLiveData.observe(
                    this@SatelliteDetailActivity,
                    { textViewCost.setClickableText(it) })
                positionLiveData.observe(
                    this@SatelliteDetailActivity,
                    { textViewLastPosition.setClickableText(it) })
                progress.observe(this@SatelliteDetailActivity,
                    { progressBar.visibility = it })
                errorMessage.observe(this@SatelliteDetailActivity,
                    { showToast(it) })
            }
        }
    }

    companion object {
        const val EXTRAS_SATELLITE_ID = "EXTRAS_SATELLITE_ID"
        const val EXTRAS_SATELLITE_NAME = "EXTRAS_SATELLITE_NAME"

        fun createIntent(
            context: Context,
            satelliteId: Int,
            satelliteName: String,
        ) = Intent(context, SatelliteDetailActivity::class.java).apply {
            putExtra(EXTRAS_SATELLITE_ID, satelliteId)
            putExtra(EXTRAS_SATELLITE_NAME, satelliteName)
        }
    }
}