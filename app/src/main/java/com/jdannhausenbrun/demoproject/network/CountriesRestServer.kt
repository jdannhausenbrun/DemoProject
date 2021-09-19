package com.jdannhausenbrun.demoproject.network

import com.jdannhausenbrun.demoproject.network.entities.CountryResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface CountriesRestServer {
    @GET("all?fields=name;alpha3Code")
    fun getCountries(): Call<List<CountryResponse>>

    object Builder {
        fun getServer(): CountriesRestServer {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(CountriesRestServer::class.java)
        }
    }
}