package com.jdannhausenbrun.demoproject.ui.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdannhausenbrun.demoproject.database.entities.Country
import com.jdannhausenbrun.demoproject.databinding.FragmentCountryListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryListFragment : Fragment() {

    private lateinit var countryListViewModel: CountryListViewModel
    private var _binding: FragmentCountryListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        countryListViewModel = ViewModelProvider(this).get(CountryListViewModel::class.java)

        _binding = FragmentCountryListBinding.inflate(inflater, container, false)

        binding.list.adapter = CountryListAdapter()
        binding.list.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch(Dispatchers.IO) {
            countryListViewModel.getCountries().collect {
                withContext(Dispatchers.Main) {
                    (binding.list.adapter as ListAdapter<Country, RecyclerView.ViewHolder>).submitList(it)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}