package com.jdannhausenbrun.demoproject.ui.countrylist

import androidx.paging.PagingSource
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.Toothpick
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

@ExperimentalCoroutinesApi
class CountryListViewModelTest {
    private lateinit var viewModel: CountryListViewModel
    private lateinit var repository: CountriesRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        val searchResults: PagingSource<Int, Country> = mockk(relaxed = true)
        coEvery { repository.searchCountries(any()) } returns searchResults

        KTP.openRootScope().installTestModules(module {
            bind(CountriesRepository::class).toInstance(repository)
        })

        viewModel = CountryListViewModel()
    }

    @Test
    fun testSyncFromNetwork() = runBlocking {
        viewModel.syncFromNetwork()
        coVerify(exactly = 1) { repository.syncCountries() }
    }

    @Test
    fun testGetsFromRepository() = runBlocking {
        viewModel.countries.first()
        verify(exactly = 1) { repository.searchCountries("") }
        viewModel.search("abc")
        viewModel.countries.first()
        verify(exactly = 1) { repository.searchCountries("abc") }
    }

    @After
    fun teardown() {
        Toothpick.reset()
        clearAllMocks()
    }
}