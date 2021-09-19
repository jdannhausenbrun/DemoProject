package com.jdannhausenbrun.demoproject.ui.countrylist

import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class CountryListViewModelTest {
    @Test
    fun testGetsFromRepository() = runBlocking {
        val repository: CountriesRepository = mockk(relaxed = true)
        coEvery { repository.getCountries() } returns
                flow { emit(listOf(Country("abc", "name"))) }

        KTP.openRootScope().installTestModules(module {
            bind(CountriesRepository::class).toInstance(repository)
        })

        val viewModel = CountryListViewModel()
        val countries = viewModel.getCountries()
        assertEquals("name", countries.first()[0].name)
    }
}