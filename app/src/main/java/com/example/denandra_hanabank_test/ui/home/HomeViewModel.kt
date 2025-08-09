package com.example.denandra_hanabank_test.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<ApiResultHandler<List<PokemonCard>>>(ApiResultHandler.Loading)
    val pokemonList: StateFlow<ApiResultHandler<List<PokemonCard>>> = _pokemonList

    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading

    private var currentPage = 1
    private val pageSize = 8
    private var isLastPage = false
    private var isLoading = false

    init {
        viewModelScope.launch {
            repository.observeCards().collect {
                _pokemonList.value = ApiResultHandler.Success(it)
            }
        }

        loadCards()
    }

    fun loadCards(reset: Boolean = false) {
        viewModelScope.launch {
            if (reset) {
                currentPage = 1
                isLastPage = false
                repository.clearAll()
            }

            val hasCache = repository.cachedCount() > 0
            _isInitialLoading.value = !hasCache && currentPage == 1

            _pokemonList.value = ApiResultHandler.Loading

            when (val res = repository.getPokemon(currentPage, pageSize)) {
                is ApiResultHandler.Success -> {
                    val added = res.data
                    if (added < pageSize) isLastPage = true else currentPage++
                }
                is ApiResultHandler.Error -> _pokemonList.value = res
                else -> Unit
            }

            _isInitialLoading.value = false
            isLoading = false
        }
    }
}