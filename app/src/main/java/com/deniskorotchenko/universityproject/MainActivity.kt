package com.deniskorotchenko.universityproject

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.deniskorotchenko.universityproject.databinding.FragmentUserlistBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: UserListViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentUserlistBinding::bind)

}