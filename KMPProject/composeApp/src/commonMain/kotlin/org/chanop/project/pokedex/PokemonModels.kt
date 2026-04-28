package org.chanop.project.pokedex

data class PokemonEntry(
    val entryNumber: Int,
    val speciesName: String,
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$entryNumber.png"
}

sealed interface PokedexUiState {
    data object Loading : PokedexUiState
    data class Success(val pokemon: List<PokemonEntry>) : PokedexUiState
    data class Error(val message: String) : PokedexUiState
}
