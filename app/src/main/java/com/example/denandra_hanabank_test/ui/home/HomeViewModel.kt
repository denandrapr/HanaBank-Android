package com.example.denandra_hanabank_test.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList =
        MutableStateFlow<ApiResultHandler<List<PokemonCard>>>(ApiResultHandler.Loading)
    val pokemonList: StateFlow<ApiResultHandler<List<PokemonCard>>> = _pokemonList

    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading

    private var currentPage = 1
    private val pageSize = 8
    private var isLastPage = false
    private var isLoading = false
    private var firstEmissionHandled = false

    init {
        viewModelScope.launch {
            val first = repository.observeCards().first()
            if (first.isEmpty()) {
                _isInitialLoading.value = true
                loadCards()
            } else {
                _pokemonList.value = ApiResultHandler.Success(first)
                currentPage = (first.size / pageSize) + 1
                isLastPage = (first.size % pageSize != 0)
                _isInitialLoading.value = false
            }
            firstEmissionHandled = true

            repository.observeCards().collect { list ->
                _pokemonList.value = ApiResultHandler.Success(list)
            }
        }
    }

    fun loadCards(reset: Boolean = false) {
        if (!reset && (isLoading || isLastPage)) return

        viewModelScope.launch {
            try {
                if (reset) {
                    repository.clearAll()
                    currentPage = 1
                    isLastPage = false
                }

                isLoading = true

                val isInitial = (currentPage == 1) && (
                        _pokemonList.value !is ApiResultHandler.Success ||
                                (_pokemonList.value as ApiResultHandler.Success).data.isNullOrEmpty()
                        )
                _isInitialLoading.value = isInitial
                _pokemonList.value = ApiResultHandler.Loading

                when (val res = repository.getPokemon(currentPage, pageSize)) {
                    is ApiResultHandler.Success -> {
                        val fetched = res.data
                        if (fetched < pageSize) {
                            isLastPage = true
                        } else {
                            currentPage++
                        }
                    }
                    is ApiResultHandler.Error -> {
                        _pokemonList.value = res
                    }
                    else -> Unit
                }
            } finally {
                _isInitialLoading.value = false
                isLoading = false
            }
        }
    }
}