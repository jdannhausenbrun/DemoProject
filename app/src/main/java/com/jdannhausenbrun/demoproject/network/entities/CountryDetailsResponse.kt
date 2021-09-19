package com.jdannhausenbrun.demoproject.network.entities

data class CountryDetailsResponse(
    val currencies: Array<CurrenciesResponse>?,
    val languages: Array<LanguagesResponse>?,
    val name: String?,
    val alpha3Code: String?,
    val capital: String?,
    val region: String?,
    val subregion: String?,
    val population: Int
)
