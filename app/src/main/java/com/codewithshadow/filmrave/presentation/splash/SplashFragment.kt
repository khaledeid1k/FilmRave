package com.codewithshadow.filmrave.presentation.splash

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.utils.DatastorePreferences
import com.codewithshadow.filmrave.databinding.FragmentSplashBinding
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    // Private properties
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launch {
            // Check if intro is completed or not using Datastore
            DatastorePreferences.getIsIntroCompleted(requireContext())
                .collect { saved ->
                    // If intro is not completed, navigate to Intro Fragment
                    if (!saved)
                        goToDestination(R.id.action_splashFragment_to_introFragment)
                    // If intro is completed, navigate to Home Fragment
                    else
                        goToDestination(R.id.action_splashFragment_to_homeFragment)
                }
        }
    }

    // Navigate to the given destination after a specified delay
    private fun goToDestination(id: Int) {
        binding.lottieAmount.addAnimatorListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                findNavController().navigate(id)
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }

        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}