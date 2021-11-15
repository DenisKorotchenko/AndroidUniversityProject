package com.deniskorotchenko.universityproject.ui.profile

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentProfileBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileFragmentViewModel by viewModels()
}