package com.codewithshadow.filmrave.presentation.home

import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.adapter.BannerAdapter
import com.codewithshadow.filmrave.core.adapter.HomeAdapter
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.core.utils.gone
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.openNetworkSetting
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.core.utils.visible
import com.codewithshadow.filmrave.databinding.FragmentHomeBinding
import com.codewithshadow.filmrave.domain.model.MovieResult
import com.codewithshadow.filmrave.presentation.base.BaseFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    // View binding instance for the HomeFragment layout
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance for getting movie info data
    private val movieInfoViewModel: MovieInfoViewModel by viewModels()

    // RecyclerView adapters and SnapHelper for the HomeFragment layout
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private var snapHelper: SnapHelper = PagerSnapHelper()

    // List to store banner movie data
    private var bannerDataList: List<MovieResult> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleClickListeners()
        observer()
    }

    private fun handleClickListeners() {
        binding.apply {

            // Navigate to SearchFragment when search button is clicked
            searchBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }

            // Navigate to WatchListFragment when favorite button is clicked
            favBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_watchListFragment)
            }

            // Open network setting when "Settings" text is clicked
            layoutNetwork.txtSetting.setOnClickListener {
                openNetworkSetting(requireContext())
            }

            // Open DetailsFragment with current visible movie info when "Watch Now" button is clicked
            watchNowBtn.setOnClickListener {
                if (isNetworkAvailable(requireActivity())) {
                    val layoutManager = binding.rvBannerImage.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    openDetailsFragment(bannerDataList[firstVisiblePosition])
                } else {
                    showToast(requireActivity(), getString(R.string.internet_connection_required))
                }

            }

        }
    }

    private fun observer() {
        movieInfoViewModel.allFeedList.observe(viewLifecycleOwner) { response ->
            when (response) {
                // If data is successfully retrieved, set adapters with data and hide loading view
                is NetworkResult.Success -> binding.apply {
                    shimmerLoading.gone()
                    response.data?.let {
                        homeFeedLayout.visible()
                        bannerDataList = it.bannerMovie
                        homeAdapter.submitList(it.homeFeedResponseList)
                        bannerAdapter.submitList(it.bannerMovie)
                    } ?: homeFeedLayout.gone()
                }

                // If there is an error retrieving data, hide loading view
                is NetworkResult.Error -> binding.apply {
                    shimmerLoading.gone()
                }

                // If data is still loading, show loading view
                is NetworkResult.Loading -> binding.apply {
                    shimmerLoading.visible()
                    homeFeedLayout.gone()
                }
            }
        }
    }

    private fun initView() {
        homeAdapter = HomeAdapter(
            onPosterClick = {
                openDetailsFragment(it)
            }
        )
        bannerAdapter = BannerAdapter()
        binding.apply {
            rvHomeFeed.adapter = homeAdapter
            rvBannerImage.adapter = bannerAdapter
            snapHelper.attachToRecyclerView(rvBannerImage)
            indicator.attachToRecyclerView(rvBannerImage)
        }
    }

    private fun openDetailsFragment(media: MovieResult) {
        // Set the required data for the details fragment using fragment result
//        setMovieResultDataInFragment(this, media)

        val bundle = Bundle()
        bundle.putString(Constants.MEDIA_SEND_REQUEST_KEY, Gson().toJson(media))

        findNavController().navigate(
            R.id.action_homeFragment_to_detailsFragment, bundle
        )

        // Navigate to the details fragment using safe navigation
//        safeFragmentNavigation(
//            navController = findNavController(),
//            currentFragmentId = R.id.homeFragment,
//            actionId = R.id.action_homeFragment_to_detailsFragment
//        )
    }

    override fun onResume() {
        super.onResume()
        // Show the network layout based on network availability
        showNetworkLayout()
    }


    private fun showNetworkLayout() {
        if (isNetworkAvailable(requireActivity())) {
            binding.layoutNetwork.layoutNtwContainer.gone()
        } else {
            binding.layoutNetwork.layoutNtwContainer.visible()
        }
    }


    override fun onNetworkLost(network: Network?) {
        super.onNetworkLost(network)
        // Show the network layout when the network is lost
        requireActivity().runOnUiThread {
            binding.layoutNetwork.layoutNtwContainer.visible()
        }
    }


    override fun onNetworkAvailable(network: Network) {
        super.onNetworkAvailable(network)
        // Hide the network layout when the network is available and perform the movieInfo api call
        requireActivity().runOnUiThread {
            binding.layoutNetwork.layoutNtwContainer.gone()
            movieInfoViewModel.getMovieInfoData() // Perform movieInfo api call when internet is available
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}