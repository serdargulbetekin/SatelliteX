package com.example.satellitex.satellitelist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.config.showToast
import com.example.satellitex.databinding.ActivitySatelliteListBinding
import com.example.satellitex.room.Satellite
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SatelliteListActivity : AppCompatActivity() {

    private val viewModel by viewModels<SatelliteListViewModel>()

    private val viewBinding by lazy {
        ActivitySatelliteListBinding.inflate(layoutInflater)
    }

    private val satelliteAdapter by lazy {
        SatelliteAdapter {
            onSatelliteItemClick(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.apply {
            recyclerView.adapter = satelliteAdapter
            viewModel.apply {
                satelliteList.observe(this@SatelliteListActivity, {
                    satelliteAdapter.submitList(it)
                })
                progress.observe(this@SatelliteListActivity, {
                    progressBar.visibility = it
                })
                errorMessage.observe(this@SatelliteListActivity, {
                    showToast(it)
                })
            }
        }
    }

    private fun onSatelliteItemClick(satellite: Satellite) {

    }

}