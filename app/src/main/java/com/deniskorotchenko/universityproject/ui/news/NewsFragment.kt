package com.deniskorotchenko.universityproject.ui.news

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentNewsBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private val viewBinding by viewBinding(FragmentNewsBinding::bind)
    private val viewModel: NewsFragmentViewModel by viewModels()
}