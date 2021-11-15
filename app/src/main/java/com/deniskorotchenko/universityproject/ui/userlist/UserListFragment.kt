package com.deniskorotchenko.universityproject.ui.userlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentUserlistBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserListFragment : BaseFragment(R.layout.fragment_userlist) {
    private val viewModel: UserListViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentUserlistBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToViewState()
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::renderViewState)
            }
        }
    }

    private fun renderViewState(viewState: UserListViewModel.ViewState) {
        when (viewState) {
            is UserListViewModel.ViewState.Loading -> {
                viewBinding.usersRecyclerView.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is UserListViewModel.ViewState.Data -> {
                viewBinding.usersRecyclerView.isVisible = true
                (viewBinding.usersRecyclerView.adapter as UserAdapter).apply {
                    userList = viewState.userList
                    notifyDataSetChanged()
                }
                viewBinding.progressBar.isVisible = false
            }
        }
    }

    private fun setupRecyclerView(): UserAdapter =
        UserAdapter().also {
            viewBinding.usersRecyclerView.adapter = it
        }
}