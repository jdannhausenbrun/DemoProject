package com.jdannhausenbrun.demoproject.ui.detail

import com.jdannhausenbrun.demoproject.network.entities.CountryDetailsResponse
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class DetailViewModelTest {
    @Test
    fun testGetsFromRepository() = runBlocking {
        val repository: CountriesRepository = mockk(relaxed = true)
        coEvery { repository.getCountryDetails(any()) } returns
                CountryDetailsResponse("name", "test", "test", "test", "test", 1)

        KTP.openRootScope().installTestModules(module {
            bind(CountriesRepository::class).toInstance(repository)
        })

        val viewModel = DetailViewModel()
        val details = viewModel.getCountryDetails("abc")
        assertEquals("name", details.first()?.name)
    }
}