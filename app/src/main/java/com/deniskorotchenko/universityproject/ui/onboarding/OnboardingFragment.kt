package com.deniskorotchenko.universityproject.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentOnboardingBinding
import com.deniskorotchenko.universityproject.onboardingTextAdapterDelegate
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dev.chrisbanes.insetter.applyInsetter

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)

    private var exoPlayer: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.volumeControlButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.signUpButton.applyInsetter {
            type(navigationBars = true) { margin() }
        }
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build().apply {
            addMediaItem(MediaItem.fromUri("asset:///onboarding_video_r.mp4"))
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
        viewBinding.playerView.player = exoPlayer
        viewBinding.viewPager.setTextPages()
        viewBinding.viewPager.attachDots(viewBinding.onboardingTextTabLayout)
        viewBinding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
        viewBinding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signUpFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        exoPlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }

    private fun ViewPager2.setTextPages() {
        adapter =
            ListDelegationAdapter(onboardingTextAdapterDelegate()).apply {
                items = listOf(
                    getString(R.string.onboarding_view_pager_text_1),
                    getString(R.string.onboarding_view_pager_text_2),
                    getString(R.string.onboarding_view_pager_text_3)
                )
            }
    }

    private fun ViewPager2.attachDots(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
    }
}