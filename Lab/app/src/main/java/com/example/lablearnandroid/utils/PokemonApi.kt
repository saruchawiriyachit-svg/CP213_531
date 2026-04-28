package com.example.lablearnandroid.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class PokemonSpecies(
    val name: String
)

data class PokemonEntry(
    val entry_number: Int,
    val pokemon_species: PokemonSpecies
)

data class PokedexResponse(
    val pokemon_entries: List<PokemonEntry>
)

interface PokemonApiService {
    @GET("pokedex/2") // Kanto pokedex in PokeAPI
    suspend fun getKantoPokedex(): PokedexResponse
}

object PokemonNetwork {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PokemonApiService = retrofit.create(PokemonApiService::class.java)
}
