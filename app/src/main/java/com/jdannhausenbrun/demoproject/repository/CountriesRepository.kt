package com.jdannhausenbrun.demoproject.repository

import com.jdannhausenbrun.demoproject.database.daos.CountryDao
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.network.CountriesRestServer
import com.jdannhausenbrun.demoproject.network.entities.CountryDetailsResponse
import kotlinx.coroutines.flow.Flow
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class CountriesRepository {
    private val countryDao: CountryDao by inject()
    private val countriesServer: CountriesRestServer by inject()

    init {
        KTP.openRootScope().inject(this)
    }

    suspend fun syncCountries() {
        val response = countriesServer.getCountries().execute()
        if (response.isSuccessful) {
            val countries = response.body()?.map {
                Country(it.alpha3Code ?: "", it.name ?: "")
            } ?: emptyList()

            countryDao.insertAll(countries)
        }
    }

    suspend fun getCountryDetails(alpha3Code: String): CountryDetailsResponse? {
        val response = countriesServer.getCountryDetails(alpha3Code).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getCountries(): Flow<List<Country>> {
        return countryDao.getAll()
    }
}