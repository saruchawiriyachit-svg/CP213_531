package org.chanop.project.pokedex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier,
) {
    Box(
        modifier = modifier.background(Color.LightGray),
    )
}
