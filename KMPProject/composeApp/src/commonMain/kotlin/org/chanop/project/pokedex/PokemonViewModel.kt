package org.chanop.project.pokedex

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonRepository(
    private val api: PokemonApi = PokemonApi(),
) {
    suspend fun getKantoPokedexEntries(): List<PokemonEntry> = api.getKantoPokedexEntries()
}

class PokemonViewModel(
    private val repository: PokemonRepository = PokemonRepository(),
) {
    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    suspend fun fetchPokemon() {
        _uiState.value = PokedexUiState.Loading
        _uiState.value = try {
            val entries = repository.getKantoPokedexEntries()
            PokedexUiState.Success(entries)
        } catch (e: Exception) {
            PokedexUiState.Error(e.message ?: "Unknown error")
        }
    }
}
