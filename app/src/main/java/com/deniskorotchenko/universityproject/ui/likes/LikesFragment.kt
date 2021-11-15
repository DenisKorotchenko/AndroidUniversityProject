package com.deniskorotchenko.universityproject.ui.likes

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentLikesBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment

class LikesFragment : BaseFragment(R.layout.fragment_likes) {

    private val viewBinding by viewBinding(FragmentLikesBinding::bind)
    private val viewModel: LikesFragmentViewModel by viewModels()
}