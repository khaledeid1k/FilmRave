package com.codewithshadow.filmrave.presentation.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.adapter.WatchListAdapter
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.gone
import com.codewithshadow.filmrave.core.utils.visible
import com.codewithshadow.filmrave.databinding.FragmentWatchListBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WatchListFragment : Fragment() {

    // View binding instance for the WatchListFragment layout
    private var _binding: FragmentWatchListBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance for getting watchlist movie info data
    private val watchListViewModel: WatchListViewModel by viewModels()
    private lateinit var watchListAdapter: WatchListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleClickListeners()
        setupObservers()
    }

    private fun handleClickListeners() {
        binding.backBtn.setOnClickListener {
            // Navigate to back when back button is clicked
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        watchListViewModel.watchList.observe(viewLifecycleOwner) {
            // If data is successfully retrieved, set adapters with data and hide loading view
            if (it.isNotEmpty()) {
                binding.layoutEmptyList.gone()
                binding.searchedResultRv.visible()
                watchListAdapter.submitList(it)
            } else {
                binding.layoutEmptyList.visible()
                binding.searchedResultRv.gone()
            }
        }
    }

    private fun initView() {
        watchListViewModel.getWatchListInfoData() // Perform watchListInfo api call when internet is available

        watchListAdapter = WatchListAdapter(onPosterClick = { media ->

            val bundle = Bundle()
            bundle.putString(Constants.MEDIA_SEND_REQUEST_KEY, Gson().toJson(media))

            findNavController().navigate(
                R.id.action_watchListFragment_to_detailsFragment, bundle
            )
        })

        binding.searchedResultRv.apply {
            adapter = watchListAdapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}