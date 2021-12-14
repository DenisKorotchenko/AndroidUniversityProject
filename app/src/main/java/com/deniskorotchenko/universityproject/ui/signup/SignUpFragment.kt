package com.deniskorotchenko.universityproject.ui.signup

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.ui.base.BaseFragment
import com.deniskorotchenko.universityproject.R
import com.deniskorotchenko.universityproject.data.network.response.error.CodeError
import com.deniskorotchenko.universityproject.databinding.FragmentSignUpBinding
import com.deniskorotchenko.universityproject.entity.ErrorCode
import com.deniskorotchenko.universityproject.util.getSpannedString
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

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
        viewBinding.backButton.setOnClickListener {
            onBackButtonPressed()
        }
        viewBinding.signUpButton.setOnClickListener {
            signUp()
        }
        viewBinding.termsAndConditionsCheckBox.setClubRulesText {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/terms")))
        }
        subscribeToFormFields()
        subscribeToEvents()
    }

    private fun signUp() {
        clearErrors()
        viewModel.signUp(
            firstName = viewBinding.firstnameEditText.text?.toString() ?: "",
            lastName = viewBinding.lastnameEditText.text?.toString() ?: "",
            userName = viewBinding.nicknameEditText.text?.toString() ?: "",
            email = viewBinding.emailEditText.text?.toString() ?: "",
            password = viewBinding.passwordEditText.text?.toString() ?: "",
            aboutMe = viewBinding.aboutMeEditText.text?.toString() ?: "",
            avatarUrl = viewBinding.avatarUrlEditText.text?.toString() ?: ""
        )
    }

    private fun showErrors(view: TextInputEditText, errors: List<CodeError>?) {
        if (errors == null)
            return
        for (error in errors) {
            view.error = error.errorMessage
        }
    }

    private fun clearErrors() {
        viewBinding.apply {
            nicknameEditText.error = null
            firstnameEditText.error = null
            lastnameEditText.error = null
            emailEditText.error = null
            passwordEditText.error = null
            aboutMeEditText.error = null
            avatarUrlEditText.error = null
        }
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is SignUpViewModel.Event.SignUpEmailConfirmationRequired -> {
                            findNavController().navigate(R.id.emailConfirmationFragment)
                        }
                        is SignUpViewModel.Event.SignUpSuccess -> {
                            findNavController().popBackStack()
                        }
                        is SignUpViewModel.Event.NetworkError -> {
                            Toast.makeText(
                                requireContext(), R.string.sign_up_network_error_toast, Toast.LENGTH_LONG
                            ).show()
                        }
                        is SignUpViewModel.Event.UnknownError -> {
                            Toast.makeText(
                                requireContext(), R.string.sign_up_unknown_error_toast, Toast.LENGTH_LONG
                            ).show()
                        }
                        is SignUpViewModel.Event.SingUpServerError -> {
                            event.error.apply {
                                showErrors(viewBinding.nicknameEditText, userName)
                                showErrors(viewBinding.lastnameEditText, lastName)
                                showErrors(viewBinding.firstnameEditText, firstName)
                                showErrors(viewBinding.emailEditText, email)
                                showErrors(viewBinding.avatarUrlEditText, avatarUrl)
                                showErrors(viewBinding.passwordEditText, password)
                                showErrors(viewBinding.aboutMeEditText, aboutMe)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onBackButtonPressed() {
        val firstname = viewBinding.firstnameEditText.text?.toString()
        val lastname = viewBinding.lastnameEditText.text?.toString()
        val nickname = viewBinding.nicknameEditText.text?.toString()
        val email = viewBinding.emailEditText.text?.toString()
        val password = viewBinding.passwordEditText.text?.toString()
        if (firstname.isNullOrBlank()
            && lastname.isNullOrBlank()
            && nickname.isNullOrBlank()
            && email.isNullOrBlank()
            && password.isNullOrBlank()
        ) {
            findNavController().popBackStack()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.common_back_alert_dialog_text)
            .setNegativeButton(R.string.common_back_alert_dialog_no_button_text) { dialog, _ ->
                dialog?.dismiss()
            }
            .setPositiveButton(R.string.common_back_alert_dialog_yes_button_text) { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }

    private fun subscribeToFormFields() {
        decideSignUpButtonEnabledState(
            firstname = viewBinding.firstnameEditText.text?.toString(),
            lastname = viewBinding.lastnameEditText.text?.toString(),
            nickname = viewBinding.nicknameEditText.text?.toString(),
            email = viewBinding.emailEditText.text?.toString(),
            password = viewBinding.passwordEditText.text?.toString(),
            termsIsChecked = viewBinding.termsAndConditionsCheckBox.isChecked
        )

        val watcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                decideSignUpButtonEnabledState(
                    firstname = viewBinding.firstnameEditText.text?.toString(),
                    lastname = viewBinding.lastnameEditText.text?.toString(),
                    nickname = viewBinding.nicknameEditText.text?.toString(),
                    email = viewBinding.emailEditText.text?.toString(),
                    password = viewBinding.passwordEditText.text?.toString(),
                    termsIsChecked = viewBinding.termsAndConditionsCheckBox.isChecked
                )
            }
        }

        viewBinding.firstnameEditText.addTextChangedListener(watcher)
        viewBinding.lastnameEditText.addTextChangedListener(watcher)
        viewBinding.nicknameEditText.addTextChangedListener(watcher)
        viewBinding.emailEditText.addTextChangedListener(watcher)
        viewBinding.passwordEditText.addTextChangedListener(watcher)
        viewBinding.termsAndConditionsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            decideSignUpButtonEnabledState(
                firstname = viewBinding.firstnameEditText.text?.toString(),
                lastname = viewBinding.lastnameEditText.text?.toString(),
                nickname = viewBinding.nicknameEditText.text?.toString(),
                email = viewBinding.emailEditText.text?.toString(),
                password = viewBinding.passwordEditText.text?.toString(),
                termsIsChecked = isChecked
            )
        }
    }

    private fun decideSignUpButtonEnabledState(
        firstname: String?,
        lastname: String?,
        nickname: String?,
        email: String?,
        password: String?,
        termsIsChecked: Boolean
    ) {
        viewBinding.signUpButton.isEnabled = !firstname.isNullOrBlank()
                && !lastname.isNullOrBlank()
                && !nickname.isNullOrBlank()
                && !email.isNullOrBlank()
                && !password.isNullOrBlank()
                && termsIsChecked
    }

    private fun CheckBox.setClubRulesText(
        clubRulesClickListener: () -> Unit
    ) {
        // Turn on ClickableSpan.
        movementMethod = LinkMovementMethod.getInstance()

        val clubRulesClickSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) = clubRulesClickListener()
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = resources.getColor(R.color.purple_200, null)
                }
            }

        text =
            resources.getSpannedString(
                R.string.sign_up_terms_and_conditions_template,
                buildSpannedString {
                    inSpans(clubRulesClickSpan) {
                        append(resources.getSpannedString(R.string.sign_up_club_rules))
                    }
                }
            )
    }
}