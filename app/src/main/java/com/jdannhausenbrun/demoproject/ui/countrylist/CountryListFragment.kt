package com.jdannhausenbrun.demoproject.ui.countrylist

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdannhausenbrun.demoproject.R
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.databinding.FragmentCountryListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

@ExperimentalCoroutinesApi
class CountryListFragment : Fragment(R.layout.fragment_country_list) {
    private val countryListViewModel: CountryListViewModel by inject()
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

        binding.list.adapter = CountryListAdapter()
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

        lifecycleScope.launch(Dispatchers.IO) {
            countryListViewModel.countries.collectLatest {
                (binding.list.adapter as PagingDataAdapter<Country, RecyclerView.ViewHolder>).submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}