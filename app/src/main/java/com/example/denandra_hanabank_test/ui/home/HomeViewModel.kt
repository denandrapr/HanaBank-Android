package com.example.denandra_hanabank_test.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList =
        MutableStateFlow<ApiResultHandler<List<PokemonCard>>>(ApiResultHandler.Loading)
    val pokemonList: StateFlow<ApiResultHandler<List<PokemonCard>>> = _pokemonList

    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private var currentPage = 1
    private val pageSize = 8
    private var isLastPage = false
    private var isLoading = false

    init {
        viewModelScope.launch {
            repository.observeCards(
                queryFlow = _query
                    .debounce(300)
                    .distinctUntilChanged()
            ).collect { list ->
                _pokemonList.value = ApiResultHandler.Success(list)
            }
        }

        loadCards()
    }

    fun onQueryChange(q: String) {
        _query.value = q
    }

    fun loadCards(reset: Boolean = false) {
        if (isLoading || isLastPage) return
        isLoading = true

        viewModelScope.launch {
            if (reset) {
                currentPage = 1
                isLastPage = false
                repository.clearAll()
            }

            val hasCache = repository.cachedCount() > 0
            _isInitialLoading.value = !hasCache && currentPage == 1

            when (val res = repository.getPokemon(currentPage, pageSize)) {
                is ApiResultHandler.Success -> {
                    val added = res.data
                    if (added < pageSize) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }
                }
                is ApiResultHandler.Error -> {
                    Log.d("TAG ", "loadCards: $res")
                    _pokemonList.value = res
                }
                else -> Unit
            }

            _isInitialLoading.value = false
            isLoading = false
        }
    }
}