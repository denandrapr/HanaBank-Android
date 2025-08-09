package com.example.denandra_hanabank_test.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.denandra_hanabank_test.R
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val homeAdapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        onClickButton()
        setView()
    }

    private fun setView() {
        binding.etSearch.setText(viewModel.query.value)

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChange(text?.toString().orEmpty())
        }
    }

    private fun onClickButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadCards()
            binding.errorContainer.visibility = View.GONE
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pokemonList.collectLatest { result ->
                        when (result) {
                            is ApiResultHandler.Loading -> {
                                handlingResultLoading()
                            }
                            is ApiResultHandler.Success -> {
                                if (result.data.isEmpty()) {
                                    binding.errorContainer.visibility = View.VISIBLE
                                    binding.rvPokemonCards.visibility = View.GONE
                                    binding.btnRetry.visibility = View.GONE
                                    binding.tvError.text = getString(R.string.data_not_found)
                                    return@collectLatest
                                }
                                binding.rvPokemonCards.visibility = View.VISIBLE
                                hideLoading()
                                homeAdapter.hideLoadingFooter()
                                binding.errorContainer.visibility = View.GONE
                                homeAdapter.submitList(result.data)
                            }
                            is ApiResultHandler.Error -> {
                                handlingResultError(result)
                            }
                        }
                    }
                }
                launch {
                    viewModel.isInitialLoading.collect { loading ->
                        binding.progressBar.isVisible = loading
                    }
                }
            }
        }
    }

    private fun handlingResultLoading() {
        if (viewModel.isInitialLoading.value) {
            showLoading()
            homeAdapter.hideLoadingFooter()
        } else {
            hideLoading()
            if (homeAdapter.itemCount > 0) {
                homeAdapter.showLoadingFooter()
            }
        }
    }

    private fun handlingResultError(result: ApiResultHandler.Error) {
        hideLoading()
        homeAdapter.hideLoadingFooter()
        if (homeAdapter.itemCount == 0) {
            binding.errorContainer.visibility = View.VISIBLE
            binding.tvError.text = result.message
        } else {
            binding.errorContainer.visibility = View.GONE
            showErrorSnackbar(result.message)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") { viewModel.loadCards() }
            .show()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPokemonCards.layoutManager = layoutManager
        binding.rvPokemonCards.adapter = homeAdapter

        binding.rvPokemonCards.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val last = lm.findLastVisibleItemPosition()
                val total = homeAdapter.itemCount
                if (last >= total - 3) {
                    viewModel.loadCards()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}