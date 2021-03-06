package com.deniskorotchenko.universityproject.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.deniskorotchenko.universityproject.BuildConfig
import com.deniskorotchenko.universityproject.util.logBackstack
import com.deniskorotchenko.universityproject.util.logFragmentHierarchy
import timber.log.Timber

open class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) {
            val logTag = "NavigationInfo"
            logFragmentHierarchy(logTag)
            try {
                findNavController().logBackstack(logTag)
            } catch (error: IllegalStateException) {
                Timber.e(error)
            }
        }
    }
}