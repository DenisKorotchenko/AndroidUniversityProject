package com.deniskorotchenko.universityproject.ui.emailconfirmation

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentEmailConfirmationBinding

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)
    private val viewModel: EmailConfirmationViewModel by viewModels()
}