package com.codewithshadow.filmrave.presentation.search

import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.adapter.HorizontalAdapter
import com.codewithshadow.filmrave.core.adapter.TopSearchesAdapter
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.core.utils.gone
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.openNetworkSetting
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.core.utils.visible
import com.codewithshadow.filmrave.databinding.FragmentSearchBinding
import com.codewithshadow.filmrave.presentation.base.BaseFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    // View binding instance for the SearchFragment layout
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance for getting searched movie info data
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var horizontalAdapter: HorizontalAdapter
    private lateinit var topSearchesAdapter: TopSearchesAdapter
    private var query: String? = null
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleClickListeners()
        setupObservers()
    }

    private fun handleClickListeners() {
        binding.apply {

            // Navigate to back when back button is clicked
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            // Open network setting when "Settings" btn is clicked
            openSettingsBtn.setOnClickListener {
                openNetworkSetting(requireContext()) // Open Network Settings
            }
        }
    }

    private fun initView() {
        searchViewModel.trendingMovies() // Perform trendingMovies api call

        topSearchesAdapter = TopSearchesAdapter(onMovieClick = { media ->

            val bundle = Bundle()
            bundle.putString(Constants.MEDIA_SEND_REQUEST_KEY, Gson().toJson(media))

            findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment, bundle
            )
        })

        binding.topSearchesRv.adapter = topSearchesAdapter


        horizontalAdapter = HorizontalAdapter(onPosterClick = { media ->

            val bundle = Bundle()
            bundle.putString(Constants.MEDIA_SEND_REQUEST_KEY, Gson().toJson(media))

            findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment, bundle
            )
        })
        binding.apply {
            searchedResultRv.adapter = horizontalAdapter
            searchQueryEt.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    query = it.trim().toString()
                    binding.apply {
                        if (it.isNotEmpty() && it.isNotBlank()) { // Make it visible on getting results
                            performSearch(it.trim().toString())
                        } else {
                            searchViewModel.trendingMovies()
                            // also cancel the job
                            job?.cancel()
                        }
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        searchViewModel.trendingMedia.observe(viewLifecycleOwner) {
            when (it) {
                // If there is an error retrieving data, hide loading view
                is NetworkResult.Error -> binding.apply {
                    progressBarOuter.gone()
                }

                // If data is still loading, show loading view when internet is available
                is NetworkResult.Loading -> binding.apply {
                    if (isNetworkAvailable(requireContext()))
                        progressBarOuter.visible()
                }

                // If data is successfully retrieved, set adapters with data and hide loading view
                is NetworkResult.Success -> binding.apply {
                    progressBarOuter.gone()
                    searchedResultRv.gone()
                    topSearchesRv.visible()
                    topSearchesAdapter.submitList(it.data?.results)
                }
            }
        }


        searchViewModel.searchedMedia.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                // If data is successfully retrieved, set adapters with data and hide loading view
                is NetworkResult.Success -> binding.apply {
                    it.data?.let { movieResponseList ->
                        if (movieResponseList.results?.isNotEmpty() == true) {
                            horizontalAdapter.submitList(it.data.results)
                            searchedResultRv.visible()
                            topSearchesRv.gone()
                        } else {
                            showToast(requireContext(), getString(R.string.no_data_found))
                        }
                    }
                }
            }
        }
    }

    private fun performSearch(searchQuery: String) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            // first let's wait for change in input search query
            delay(800)
            searchViewModel.searchMedia(
                searchQuery = searchQuery
            )
        }
    }


    override fun onResume() {
        super.onResume()
        showNetworkLayout()
    }


    private fun showNetworkLayout() {
        if (isNetworkAvailable(requireActivity())) {
            binding.progressBarOuter.visible()
            binding.layoutNetwork.gone()
        } else {
            binding.progressBarOuter.gone()
            binding.layoutNetwork.visible()
        }
    }


    override fun onNetworkLost(network: Network?) {
        super.onNetworkLost(network)
        // Show the network layout when the network is lost
        requireActivity().runOnUiThread {
            binding.layoutNetwork.visible()
        }
    }


    override fun onNetworkAvailable(network: Network) {
        super.onNetworkAvailable(network)
        // Hide the network layout when the network is available and perform the movieInfo api call
        requireActivity().runOnUiThread {
            binding.layoutNetwork.gone()
            searchViewModel.trendingMovies()  // Perform trendingMovies api call when internet is available
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}