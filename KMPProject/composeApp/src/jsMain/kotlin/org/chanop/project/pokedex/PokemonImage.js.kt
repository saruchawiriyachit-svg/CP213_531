package org.chanop.project.pokedex

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.WebElementView
import androidx.compose.ui.unit.dp
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier,
) {
    WebElementView(
        modifier = modifier
            .background(
                color = Color(0xFFFFF4E5),
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.dp,
                color = Color(0xFFFFC78A),
                shape = RoundedCornerShape(8.dp),
            ),
        factory = {
            (document.createElement("img") as HTMLImageElement).apply {
                src = imageUrl
                alt = contentDescription
                style.width = "64px"
                style.height = "64px"
                style.objectFit = "contain"
                style.borderRadius = "8px"
                style.display = "block"
                style.backgroundColor = "#FFF4E5"
                style.border = "1px solid #FFC78A"
            }
        },
        update = { image ->
            image.src = imageUrl
            image.alt = contentDescription
        },
    )
}
