package com.jdannhausenbrun.demoproject.ui.countrylist

import androidx.lifecycle.ViewModel
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class CountryListViewModel : ViewModel() {
    private val countriesRepository: CountriesRepository by inject()

    init {
        KTP.openRootScope().inject(this)
    }

    suspend fun getCountries(): Flow<List<Country>> {
        return countriesRepository.getCountries()
    }
}