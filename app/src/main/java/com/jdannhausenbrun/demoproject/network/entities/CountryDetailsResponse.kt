package com.jdannhausenbrun.demoproject.network.entities

data class CountryDetailsResponse(
    val name: String?,
    val alpha3Code: String?,
    val capital: String?,
    val continent: String?,
    val region: String?,
    val population: Int,
    val flags: Array<String>?
)
