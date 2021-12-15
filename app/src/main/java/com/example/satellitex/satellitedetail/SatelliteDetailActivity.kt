package com.example.satellitex.satellitedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitex.databinding.ActivitySatelliteDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SatelliteDetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<SatelliteDetailViewModel>()

    private val viewBinding by lazy {
        ActivitySatelliteDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    companion object {
        const val EXTRAS_SATELLITE_ID = "EXTRAS_SATELLITE_ID"

        fun createIntent(
            context: Context,
            satelliteId: Int
        ) = Intent(context, SatelliteDetailActivity::class.java).apply {
            putExtra(EXTRAS_SATELLITE_ID, satelliteId)
        }
    }
}