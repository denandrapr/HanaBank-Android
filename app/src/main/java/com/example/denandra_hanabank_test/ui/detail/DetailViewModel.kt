package com.example.denandra_hanabank_test.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val ARG_CARD_ID = "card_id"
    }

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    init {
        val id: String? = savedStateHandle[ARG_CARD_ID]
        if (!id.isNullOrBlank()) load(id)
    }

    fun load(id: String) {
        viewModelScope.launch {
            repository.getCardById(id).collect { result ->
                when (result) {
                    is ApiResultHandler.Loading -> _state.value = _state.value.copy(loading = true, error = null)
                    is ApiResultHandler.Success -> _state.value = DetailState(card = result.data)
                    is ApiResultHandler.Error -> _state.value = DetailState(error = result.message)
                }
            }
        }
    }
}

data class DetailState(
    val loading: Boolean = false,
    val card: PokemonCard? = null,
    val error: String? = null
)