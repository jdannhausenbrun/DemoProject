package com.jdannhausenbrun.demoproject.database.daos

import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import com.jdannhausenbrun.demoproject.DemoApplication
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.utils.TestDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountryDaoTest {
    private lateinit var testDB: TestDB

    @Before
    fun setup() {
        val application = ApplicationProvider.getApplicationContext<DemoApplication>()
        testDB = TestDB.createTestDB(application)
    }

    @Test
    fun testInsertAndRetrieve() = runBlocking {
        var result = testDB.countryDao().getAll().first()
        assertEquals(0, result.size)

        testDB.countryDao().insertAll(
            listOf(
                Country("cd1", "Country 1"),
                Country("cd2", "Country 2")
            )
        )

        result = testDB.countryDao().getAll().first()
        assertEquals(2, result.size)
        assertEquals("Country 1", result[0].name)
        assertEquals("Country 2", result[1].name)
    }

    @Test
    fun testReplaceOnInsert() = runBlocking {
        testDB.countryDao().insertAll(
            listOf(
                Country("cd1", "Country 1"),
                Country("cd2", "Country 2")
            )
        )

        var result = testDB.countryDao().getAll().first()
        assertEquals(2, result.size)

        testDB.countryDao().insertAll(
            listOf(
                Country("cd2", "Country 22")
            )
        )

        result = testDB.countryDao().getAll().first()
        assertEquals(2, result.size)
        assertEquals("Country 22", result[1].name)
    }

    @Test
    fun testEmptySearch() = runBlocking {
        val data = listOf(
            Country("cd1", "Country 1"),
            Country("cd2", "Country 2")
        )

        testDB.countryDao().insertAll(data)

        val result = testDB.countryDao().getByName("").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        assertEquals(expected, result)
    }

    @Test
    fun testSearchWithResults() = runBlocking {
        val data = listOf(
            Country("cd1", "Country 1"),
            Country("cd2", "Country 2")
        )

        testDB.countryDao().insertAll(data)

        val result = testDB.countryDao().getByName("1").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(data[0]),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        assertEquals(expected, result)
    }

    @Test
    fun testSearchWithNoResults() = runBlocking {
        val data = listOf(
            Country("cd1", "Country 1"),
            Country("cd2", "Country 2")
        )

        testDB.countryDao().insertAll(data)

        val result = testDB.countryDao().getByName("123").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )
        assertEquals(expected, result)
    }
}