package com.example.denandra_hanabank_test.ui.detail

import androidx.lifecycle.ViewModel
import com.example.denandra_hanabank_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: PokemonRepository
) : ViewModel() {

}