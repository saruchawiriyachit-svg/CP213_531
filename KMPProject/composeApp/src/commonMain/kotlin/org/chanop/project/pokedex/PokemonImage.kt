package org.chanop.project.pokedex

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
)
