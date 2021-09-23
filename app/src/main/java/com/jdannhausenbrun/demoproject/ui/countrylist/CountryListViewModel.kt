package com.jdannhausenbrun.demoproject.ui.countrylist

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

@ExperimentalCoroutinesApi
class CountryListViewModel : ViewModel() {
    private val PAGE_SIZE = 20
    private val countriesRepository: CountriesRepository by inject()
    private val searchQuery = MutableStateFlow("")

    init {
        KTP.openRootScope().inject(this)
    }

    val countries: Flow<PagingData<Country>> = searchQuery.debounce(200)
        .distinctUntilChanged()
        .flatMapLatest {
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                countriesRepository.searchCountries(it)
            }.flow
        }

    fun search(query: String?) {
        searchQuery.value = query ?: ""
    }
}