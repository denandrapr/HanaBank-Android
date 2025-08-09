package com.example.denandra_hanabank_test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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

    private var isLoading = false
    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
        setupActions()

        binding.etSearch.addTextChangedListener {
            homeAdapter.setQuery(it.toString())
        }

        homeAdapter.onFilterResult = { isEmpty, hasQuery ->
            if (!isLoading && hasQuery && isEmpty) {
                binding.errorContainer.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.GONE
                binding.tvError.text = getString(R.string.no_results_found)
            } else {
                binding.errorContainer.visibility = View.GONE
            }
        }
    }

    private fun setupActions() {
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
                                isLoading = true
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

                            is ApiResultHandler.Success -> {
                                isLoading = false
                                hideLoading()
                                homeAdapter.hideLoadingFooter()
                                homeAdapter.submitList(result.data)

                                isLastPage = result.data.isNotEmpty() && (result.data.size % 8 != 0)
                            }

                            is ApiResultHandler.Error -> {
                                isLoading = false
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
                        }
                    }
                }
                launch {
                    viewModel.isInitialLoading.collectLatest { initial ->
                        if (initial) showLoading() else hideLoading()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPokemonCards.apply {
            this.layoutManager = layoutManager
            adapter = homeAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return
                    if (isLoading || isLastPage) return

                    val total = layoutManager.itemCount
                    val last = layoutManager.findLastVisibleItemPosition()
                    if (total <= last + 2) {
                        viewModel.loadCards()
                    }
                }
            })
        }
    }

    private fun showLoading() { binding.progressBar.visibility = View.VISIBLE }
    private fun hideLoading() { binding.progressBar.visibility = View.GONE }

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
