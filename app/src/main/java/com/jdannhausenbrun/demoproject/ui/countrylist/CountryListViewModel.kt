package com.jdannhausenbrun.demoproject.ui.countrylist

import androidx.lifecycle.ViewModel
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

@ExperimentalCoroutinesApi
class CountryListViewModel : ViewModel() {
    private val countriesRepository: CountriesRepository by inject()
    private val searchQuery = MutableStateFlow("")

    init {
        KTP.openRootScope().inject(this)
    }

    val countries: Flow<List<Country>> = searchQuery.debounce(200)
        .distinctUntilChanged()
        .flatMapLatest {
            countriesRepository.searchCountries(it)
        }

    fun search(query: String?) {
        searchQuery.value = query ?: ""
    }
}