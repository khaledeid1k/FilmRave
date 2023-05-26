package com.codewithshadow.filmrave.presentation.details

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.adapter.RecommendationsAdapter
import com.codewithshadow.filmrave.core.utils.Constants.BASE_YOUTUBE_URL
import com.codewithshadow.filmrave.core.utils.Constants.MEDIA_SEND_REQUEST_KEY
import com.codewithshadow.filmrave.core.utils.Constants.TEASER
import com.codewithshadow.filmrave.core.utils.Constants.TMDB_IMAGE_BASE_URL_W780
import com.codewithshadow.filmrave.core.utils.Constants.TRAILER
import com.codewithshadow.filmrave.core.utils.Constants.YOUTUBE
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.core.utils.formatMediaDate
import com.codewithshadow.filmrave.core.utils.getLanguageTitle
import com.codewithshadow.filmrave.core.utils.getMovieGenreListFromIds
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.core.utils.showSharingDialog
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.databinding.FragmentDetailsBinding
import com.codewithshadow.filmrave.domain.model.MovieResult
import com.codewithshadow.filmrave.domain.model.MovieVideoResult
import com.codewithshadow.filmrave.presentation.home.MovieInfoViewModel
import com.codewithshadow.filmrave.presentation.watchlist.WatchListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private val movieInfoViewModel: MovieInfoViewModel by viewModels()
    private val watchListViewModel: WatchListViewModel by viewModels()

    private lateinit var movieResult: MovieResult
    private var mediaId: Int? = null
    private var youtubeUrl: String = ""
    private var isAddedToWatchList = false
    private var youTubePlayer: YouTubePlayer? = null
    private var youTubePlayerListener: AbstractYouTubePlayerListener? = null
    private var watchProviderUrl: String? = null
    private val customTabsIntent by lazy {
        CustomTabsIntent.Builder().setShowTitle(true).build()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.SheetDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpFragmentResultListeners()
        handleClickListeners()
        setupObservers()
    }

    private fun handleClickListeners() {
        binding.apply {
            addToWatchlistBtn.setOnClickListener {
                if (isAddedToWatchList) {
                    // Delete Movie Data in RoomDb
                    watchListViewModel.deleteWatchListInfoData(movieResult)
                    addButtonIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_add
                        )
                    )
                    showToast(requireContext(), getString(R.string.movie_deleted))
                    dismiss()
                } else {
                    // Insert Movie Data in RoomDb
                    watchListViewModel.insertWatchListInfoData(movieResult)
                    addButtonIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.baseline_done_all_24
                        )
                    )
                    showToast(requireContext(), getString(R.string.movie_added))
                }
                isAddedToWatchList = !isAddedToWatchList
            }

            watchButton.setOnClickListener {
                watchProviderUrl?.let { url -> // Open URL in CustomTabs
                    customTabsIntent.launchUrl(requireContext(), url.toUri())
                } ?: showToast(requireContext(), getString(R.string.information_not_available))
            }

            shareBtn.setOnClickListener {
                showSharingDialog(
                    requireActivity(),
                    movieResult.title.toString(),
                    youtubeUrl
                ) // Open Sharing Dialog
            }
        }
    }

    private fun openDetailsFragment(media: MovieResult) {
        val bundle = Bundle()
        bundle.putString(MEDIA_SEND_REQUEST_KEY, Gson().toJson(media))

        findNavController().navigate(
            R.id.action_detailsFragment_self, bundle
        )
    }

    private fun initView() {
        recommendationsAdapter = RecommendationsAdapter(onPosterClick = {
            openDetailsFragment(it)
        })
        binding.rvRecommendedList.adapter = recommendationsAdapter
    }

    private fun setupObservers() {
        movieInfoViewModel.recommendedMoviesList.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> binding.apply {
                    it.data?.let { result ->
                        result.results.let { it1 ->
                            if (it1 != null && it.data.results?.isNotEmpty() == true) {
                                binding.recommendedText.text = getString(R.string.more_like_this)
                                recommendationsAdapter.submitList(it1)
                            } else {
                                binding.recommendedText.text =
                                    getString(R.string.recommendations_not_available)
                            }
                        }
                    }
                }
            }
        }

        movieInfoViewModel.movieResponseVideoList.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> binding.apply {
                    it.data?.let { result ->
                        result.results.let { it1 ->
                            if (it1 != null) {
                                val totalVideos = it.data.results as ArrayList
                                val trailers: List<MovieVideoResult> =
                                    totalVideos.filter { toFilter ->
                                        (toFilter.type == TRAILER || toFilter.type == TEASER) && toFilter.site == YOUTUBE
                                    }
                                try {
                                    val trailer =
                                        if (trailers.isEmpty()) totalVideos[0] else trailers[0]
                                    youtubeUrl = "$BASE_YOUTUBE_URL${trailer.key}"
                                    initializePlayer(trailer.key)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        }
                    }
                }
            }
        }

        watchListViewModel.watchListProviders.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> binding.apply {}
                is NetworkResult.Success -> binding.apply {
                    it.data?.let { result ->
                        result.results.let { value ->
                            if (value != null) {
                                watchProviderUrl = value.iN?.link
                            }
                        }
                    }
                }
            }
        }

        watchListViewModel.watchList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                var isAddedToWatchList: Boolean
                for (result in it) {
                    isAddedToWatchList = (result.id == mediaId)
                    if (isAddedToWatchList) {
                        checkIsWatchListAdded()
                        break
                    }
                }
            }
        }
    }

    private fun initializePlayer(videoKey: String?) = binding.apply {
        if (youTubePlayerListener != null) {
            if (videoKey != null) {
                youTubePlayer?.loadVideo(videoKey, 0f)
            }
        }

        youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoKey?.let {
                    this@DetailsFragment.youTubePlayer = youTubePlayer
                    this@DetailsFragment.youTubePlayer?.loadVideo(it, 0f)
                }
            }
        }

        binding.videoView.apply {
            addYouTubePlayerListener(youTubePlayerListener!!)
        }
    }


    private fun setUpFragmentResultListeners() {
        val args = Gson().fromJson(
            arguments?.getString(MEDIA_SEND_REQUEST_KEY),
            MovieResult::class.java
        )
        args?.let {
            mediaId = args.id
            movieResult = args

            val genreList: List<Int>? = args.genreIds
            val title = args.title
            val overview = args.overview
            val imgUrl = args.id
            val year = args.releaseDate
            val rating = args.voteAverage
            val language = args.originalLanguage

            binding.apply {
                tvGenres.text = getMovieGenreListFromIds(genreList)
                    .joinToString("  •  ") { it.name }
                posterImage.loadImage(TMDB_IMAGE_BASE_URL_W780.plus(imgUrl))
                titleText.text = title
                releaseDate.formatMediaDate(year)
                ratingText.text = String.format("%.1f", rating)
                overviewText.text = overview
                languageText.text = language?.let { getLanguageTitle(it) }
            }
            mediaId?.let {
                movieInfoViewModel.getMovieTrailerData(it)
                movieInfoViewModel.getRecommendedMovies(it)
                watchListViewModel.getWatchListInfoData()
                watchListViewModel.getWatchListProviders(movieId = it)
            }
        }
    }


    private fun checkIsWatchListAdded() {
        binding.apply {
            isAddedToWatchList = true
            addButtonIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.baseline_done_all_24
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        youTubePlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        youTubePlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
        youTubePlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.release()
        _binding = null
    }

}