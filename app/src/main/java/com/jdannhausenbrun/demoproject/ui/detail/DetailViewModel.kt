package com.jdannhausenbrun.demoproject.ui.detail

import androidx.lifecycle.ViewModel
import com.jdannhausenbrun.demoproject.network.entities.CountryDetailsResponse
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class DetailViewModel : ViewModel() {
    private val countriesRepository: CountriesRepository by inject()

    init {
        KTP.openRootScope().inject(this)
    }

    fun getCountries(alpha3Code: String): Flow<CountryDetailsResponse?> {
        return flow {
            emit(countriesRepository.getCountryDetails(alpha3Code))
        }
    }
}