package com.jdannhausenbrun.demoproject.repository

import com.jdannhausenbrun.demoproject.database.daos.CountryDao
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.network.CountriesRestServer
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
}