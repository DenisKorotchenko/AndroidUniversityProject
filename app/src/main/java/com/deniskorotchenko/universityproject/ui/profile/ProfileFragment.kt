package com.deniskorotchenko.universityproject.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentProfileBinding
import com.deniskorotchenko.universityproject.entity.User
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEvents()
        subscribeToUserData()
        viewBinding.logoutButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun subscribeToUserData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::renderProfile)
            }
        }
    }

    private fun renderProfile(viewState: ProfileFragmentViewModel.ViewState) {
        when (viewState) {
            is ProfileFragmentViewModel.ViewState.Loading -> {
                viewBinding.userNameTextView.text = getString(R.string.profile_loading_text)
                viewBinding.groupNameTextView.text = getString(R.string.profile_loading_text)
                Glide.with(viewBinding.avatarImageView)
                    .load(R.drawable.ic_android_black_24dp)
                    .into(viewBinding.avatarImageView)
            }
            is ProfileFragmentViewModel.ViewState.Error -> {
                viewBinding.userNameTextView.text = getString(R.string.profile_error_text)
                viewBinding.groupNameTextView.text = getString(R.string.profile_error_text)
                Glide.with(viewBinding.avatarImageView)
                    .load(R.drawable.ic_android_black_24dp)
                    .into(viewBinding.avatarImageView)
            }
            is ProfileFragmentViewModel.ViewState.Data -> {
                val profile = viewState.userData
                viewBinding.userNameTextView.text = profile.userName
                viewBinding.groupNameTextView.text = profile.groupName
                Glide.with(viewBinding.avatarImageView)
                    .load(profile.avatarUrl)
                    .circleCrop()
                    .into(viewBinding.avatarImageView)
            }
        }
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is ProfileFragmentViewModel.Event.LogoutError -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    R.string.common_general_error_text,
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }
            }
        }
    }
}