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
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
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
        val shouldReset = homeAdapter.itemCount == 0
        viewModel.loadCards(reset = shouldReset)

        binding.errorContainer.isVisible = false
        binding.progressBar.isVisible = true
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
                                handlingResultSuccess(result)
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

    private fun handlingResultSuccess(result: ApiResultHandler.Success<List<PokemonCard>>) {
        val list = result.data
        val isSearching = viewModel.query.value.isNotBlank()
        val isInitial = viewModel.isInitialLoading.value

        homeAdapter.hideLoadingFooter()

        if (isInitial) {
            binding.errorContainer.isVisible = false
            if (list.isNotEmpty()) {
                binding.rvPokemonCards.isVisible = true
                homeAdapter.submitList(list)
            }
            return
        }

        if (list.isEmpty()) {
            binding.rvPokemonCards.isVisible = false
            binding.errorContainer.isVisible = true
            binding.btnRetry.isVisible = !isSearching
            binding.tvError.text = if (isSearching)
                getString(R.string.data_not_found)
            else
                getString(R.string.error_memuat_data)
        } else {
            binding.errorContainer.isVisible = false
            binding.rvPokemonCards.isVisible = true
            homeAdapter.submitList(list)
        }
    }

    private fun handlingResultLoading() {
        val isInitial = viewModel.isInitialLoading.value
        val isSearching = viewModel.query.value.isNotBlank()

        if (isInitial) {
            binding.errorContainer.isVisible = false
            homeAdapter.hideLoadingFooter()
        } else {
            if (!isSearching && homeAdapter.itemCount > 0) {
                homeAdapter.showLoadingFooter()
            } else {
                homeAdapter.hideLoadingFooter()
            }
        }
    }

    private fun handlingResultError(result: ApiResultHandler.Error) {
        hideLoading()
        homeAdapter.hideLoadingFooter()

        val hasItems = homeAdapter.itemCount > 0

        if (!hasItems) {
            binding.errorContainer.isVisible = true
            binding.rvPokemonCards.isVisible = false
            binding.btnRetry.isVisible = true
            binding.tvError.text = result.message
        } else {
            binding.errorContainer.isVisible = false
            binding.rvPokemonCards.isVisible = true
            showErrorSnackbar(result.message)
        }
    }

    private fun setupRecyclerView() {
        val lm = LinearLayoutManager(requireContext())
        binding.rvPokemonCards.layoutManager = lm
        binding.rvPokemonCards.adapter = homeAdapter

        val threshold = 4
        binding.rvPokemonCards.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return

                val isSearching = viewModel.query.value.isNotBlank()
                if (isSearching) return

                val last = lm.findLastVisibleItemPosition()
                val total = homeAdapter.itemCount
                val nearEnd = last >= total - threshold
                val cannotScrollFurther = !rv.canScrollVertically(1)

                if (nearEnd && cannotScrollFurther) {
                    viewModel.loadCards()
                }
            }
        })
    }

    private fun showLoading() { binding.progressBar.isVisible = true }
    private fun hideLoading() { binding.progressBar.isVisible = false }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") { viewModel.loadCards() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}