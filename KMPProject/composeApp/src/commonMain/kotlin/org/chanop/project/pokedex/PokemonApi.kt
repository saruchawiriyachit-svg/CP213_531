package org.chanop.project.pokedex

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PokemonApi(
    private val client: HttpClient = HttpClient(),
) {
    suspend fun getKantoPokedexEntries(): List<PokemonEntry> {
        val payload: String = client.get("https://pokeapi.co/api/v2/pokedex/2/").body()
        val root = Json.parseToJsonElement(payload).jsonObject
        val entries = root["pokemon_entries"]?.jsonArray.orEmpty()

        return entries.mapNotNull { element ->
            val entryObject = element.jsonObject
            val number = entryObject["entry_number"]?.jsonPrimitive?.intOrNull ?: return@mapNotNull null
            val speciesName = entryObject["pokemon_species"]
                ?.jsonObject
                ?.get("name")
                ?.jsonPrimitive
                ?.contentOrNull
                ?: return@mapNotNull null
            PokemonEntry(entryNumber = number, speciesName = speciesName)
        }
    }
}
