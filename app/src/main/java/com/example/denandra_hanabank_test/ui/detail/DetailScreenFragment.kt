package com.example.denandra_hanabank_test.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.denandra_hanabank_test.databinding.FragmentDetailScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailScreenFragment : Fragment() {
    private var _binding: FragmentDetailScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

}