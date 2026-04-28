package org.chanop.project.pokedex

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
actual fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
