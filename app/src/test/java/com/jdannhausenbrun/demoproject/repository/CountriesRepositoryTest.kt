package com.jdannhausenbrun.demoproject.repository

import com.jdannhausenbrun.demoproject.database.daos.CountryDao
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.network.CountriesRestServer
import com.jdannhausenbrun.demoproject.network.entities.CountryDetailsResponse
import com.jdannhausenbrun.demoproject.network.entities.CountryResponse
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import toothpick.Toothpick
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class CountriesRepositoryTest {
    private lateinit var dao: CountryDao
    private lateinit var restServer: CountriesRestServer
    private lateinit var repository: CountriesRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        restServer = mockk(relaxed = true)

        KTP.openRootScope().installTestModules(module {
            bind(CountryDao::class).toInstance(dao)
            bind(CountriesRestServer::class).toInstance(restServer)
        })

        repository = CountriesRepository()
    }

    @Test
    fun testSyncCountries() = runBlocking {
        every { restServer.getCountries().execute() } returns Response.success(200, listOf(CountryResponse("cod", "Name")))
        repository.syncCountries()
        verify(exactly = 1) { dao.insertAll(listOf(Country("cod", "Name"))) }
    }

    @Test
    fun testSyncCountriesError() = runBlocking {
        every { restServer.getCountries().execute() } returns Response.error(400, ResponseBody.create(MediaType.get("text/plain"), "test"))
        repository.syncCountries()
        verify(exactly = 0) { dao.insertAll(any()) }
    }

    @Test
    fun testGetDetails() = runBlocking {
        every { restServer.getCountryDetails(any()).execute() } returns Response.success(200, CountryDetailsResponse("name", "tes", "test", "test", "test", 1))
        assertEquals("name", repository.getCountryDetails("tes")?.name)
    }

    @Test
    fun testGetDetailsError() = runBlocking {
        every { restServer.getCountryDetails(any()).execute() } returns Response.error(400, ResponseBody.create(MediaType.get("text/plain"), "test"))
        assertNull(repository.getCountryDetails("test"))
    }

    @Test
    fun testGetCountries() = runBlocking {
        repository.getCountries()
        coVerify(exactly = 1) { dao.getAll() }
    }

    @After
    fun teardown() {
        Toothpick.reset()
        clearAllMocks()
    }
}