package com.jdannhausenbrun.demoproject.ui.countrylist

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdannhausenbrun.demoproject.R
import com.jdannhausenbrun.demoproject.databinding.FragmentCountryListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

@ExperimentalCoroutinesApi
class CountryListFragment : Fragment(R.layout.fragment_country_list) {
    private val countryListViewModel: CountryListViewModel by inject()
    private lateinit var adapter: CountryListAdapter
    private lateinit var loadStateListener: (CombinedLoadStates) -> Unit
    private var _binding: FragmentCountryListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KTP.openRootScope().openSubScope(CountryListViewModel::class.java)
            .installViewModelBinding<CountryListViewModel>(this)
            .closeOnViewModelCleared(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountryListBinding.bind(view)

        // In the future, convert to a sync service with periodic and forced syncs
        lifecycleScope.launch(Dispatchers.IO) {
            countryListViewModel.syncFromNetwork()
        }

        adapter = CountryListAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        binding.searchView.queryHint = requireContext().getString(R.string.country_search_hint)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                countryListViewModel.search(query)
                return true
            }
        })

        loadStateListener = { loadState ->
            val isDataSetEmpty = loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1
            binding.noResults.isVisible = isDataSetEmpty
            binding.list.isVisible = !isDataSetEmpty
        }
        adapter.addLoadStateListener(loadStateListener)

        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                countryListViewModel.countries.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeLoadStateListener(loadStateListener)
        _binding = null
    }
}