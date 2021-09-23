package com.jdannhausenbrun.demoproject.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.request.ImageRequest
import com.jdannhausenbrun.demoproject.R
import com.jdannhausenbrun.demoproject.common.CoilSVGLoader
import com.jdannhausenbrun.demoproject.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel: DetailViewModel by inject()
    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KTP.openRootScope().openSubScope(DetailViewModel::class.java)
            .installViewModelBinding<DetailViewModel>(this)
            .closeOnViewModelCleared(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                detailViewModel.getCountryDetails(args.type).collect {
                    withContext(Dispatchers.Main) {
                        CoilSVGLoader.getInstance(requireContext()).enqueue(
                            ImageRequest.Builder(requireContext())
                                .data(it?.flags?.first())
                                .target { drawable ->
                                    binding.flagView.setImageDrawable(drawable)
                                    binding.flagView.visibility = View.VISIBLE
                                }.build()
                        )

                        val missingData = requireContext().getString(R.string.double_dash)
                        binding.name.text = it?.name ?: missingData
                        binding.capital.text = requireContext().getString(R.string.capital_display).format(it?.capital ?: missingData)
                        binding.continent.text = requireContext().getString(R.string.continent_display).format(it?.continent ?: missingData)
                        binding.region.text = requireContext().getString(R.string.region_display).format(it?.region ?: missingData)
                        binding.population.text = requireContext().getString(R.string.population_display).format(it?.population ?: missingData)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}