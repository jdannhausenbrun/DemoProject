package com.jdannhausenbrun.demoproject.network.entities

data class CountryDetailsResponse(
    val name: String?,
    val alpha3Code: String?,
    val capital: String?,
    val region: String?,
    val subregion: String?,
    val population: Int,
    val flag: String?
)
