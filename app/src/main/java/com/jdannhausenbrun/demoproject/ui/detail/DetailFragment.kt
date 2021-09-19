package com.jdannhausenbrun.demoproject.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jdannhausenbrun.demoproject.R
import com.jdannhausenbrun.demoproject.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel: DetailViewModel by inject()
    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        KTP.openRootScope().openSubScope(DetailViewModel::class.java)
            .installViewModelBinding<DetailViewModel>(this)
            .closeOnViewModelCleared(this)
            .inject(this)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            detailViewModel.getCountryDetails(args.type).collect {
                withContext(Dispatchers.Main) {
                    binding.name.text = it?.name ?: ""
                    binding.capital.text = requireContext().getString(R.string.capital_display).format(it?.capital ?: "")
                    binding.region.text = requireContext().getString(R.string.region_display).format(it?.region ?: "")
                    binding.subRegion.text = requireContext().getString(R.string.sub_region_display).format(it?.subregion ?: "")
                    binding.population.text = requireContext().getString(R.string.population_display).format(it?.population ?: "")
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