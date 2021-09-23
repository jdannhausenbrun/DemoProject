package com.jdannhausenbrun.demoproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jdannhausenbrun.demoproject.databinding.ActivityMainBinding
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class MainActivity : AppCompatActivity() {
    private val countriesRepository: CountriesRepository by inject()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KTP.openRootScope().inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // In the future, convert to a sync service with periodic and forced syncs
        lifecycleScope.launch(Dispatchers.IO) {
            countriesRepository.syncCountries()
        }
    }
}