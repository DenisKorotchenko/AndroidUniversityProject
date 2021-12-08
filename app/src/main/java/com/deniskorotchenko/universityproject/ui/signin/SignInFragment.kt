package com.deniskorotchenko.universityproject.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.databinding.FragmentSignInBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewBinding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackButtonPressed()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            signInButton.setOnClickListener {
                viewModel.signIn(
                    email = viewBinding.emailEditText.text?.toString() ?: "",
                    password = viewBinding.passwordEditText.text?.toString() ?: ""
                )
            }
            backButton.setOnClickListener {
                onBackButtonPressed()
            }
        }
        setupAnimation()
        subscribeToFormFields()
    }

    private fun setupAnimation() {
        val firstRotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        val repeatableRotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.repeatable_rotate)
        val enableButtonAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)

        enableButtonAnimation.setAnimationListener( object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                viewBinding.signInButton.alpha = 1.0.toFloat()
            }

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

        })

        firstRotateAnimation.setAnimationListener( object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                viewBinding.signInButton.startAnimation(enableButtonAnimation)
                viewBinding.mknLogoImageView.startAnimation(repeatableRotateAnimation)
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        viewBinding.mknLogoImageView.startAnimation(firstRotateAnimation)
        //viewBinding.signInButton.startAnimation(enableButtonAnimation)
    }

    private fun onBackButtonPressed() {
        if (viewBinding.emailEditText.text?.toString().isNullOrBlank() && viewBinding.passwordEditText.text?.toString().isNullOrBlank()) {
            findNavController().popBackStack()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.common_back_alert_dialog_text)
            .setNegativeButton(R.string.common_back_alert_dialog_no_button_text) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_back_alert_dialog_yes_button_text) { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }

    private fun subscribeToFormFields() {
        decideSignInButtonEnabledState(
            email = viewBinding.emailEditText.text?.toString(),
            password = viewBinding.passwordEditText.text?.toString()
        )
        viewBinding.emailEditText.doAfterTextChanged { email ->
            decideSignInButtonEnabledState(
                email = email?.toString(),
                password = viewBinding.passwordEditText.text?.toString()
            )
        }
        viewBinding.passwordEditText.doAfterTextChanged { password ->
            decideSignInButtonEnabledState(
                email = viewBinding.emailEditText.text?.toString(),
                password = password?.toString()
            )
        }
    }

    private fun decideSignInButtonEnabledState(email: String?, password: String?) {
        viewBinding.signInButton.isEnabled =
            !(email.isNullOrBlank() || password.isNullOrBlank())
    }
}