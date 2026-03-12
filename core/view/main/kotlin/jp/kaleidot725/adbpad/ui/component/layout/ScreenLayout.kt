package jp.kaleidot725.adbpad.ui.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ScreenLayout(
    top: (@Composable () -> Unit)? = null,
    navigationRail: @Composable () -> Unit,
    content: @Composable () -> Unit,
    right: (@Composable () -> Unit)? = null,
    dialog: @Composable () -> Unit,
    bottom: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.background
    val accent = MaterialTheme.colorScheme.primary
    Box(
        modifier.background(
            Brush.linearGradient(
                colors =
                    listOf(
                        accent.copy(alpha = 0.05f),
                        accent.copy(alpha = 0.05f),
                    ),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
            ),
        ),
    ) {
        Column {
            if (top != null) {
                top()
            }
            Row(modifier = Modifier.weight(1f)) {
                Box { navigationRail() }
                Box(
                    Modifier
                        .weight(1f)
                        .padding(8.dp),
                ) {
                    content()
                }
                if (right != null) { Box { right() } }
            }
            if (bottom != null) {
                bottom()
            }
        }
        dialog()
    }
}

@Preview
@Composable
private fun ScreenLayout_Preview() {
    ScreenLayout(
        navigationRail = {
            Box(Modifier.width(50.dp).fillMaxHeight().background(androidx.compose.ui.graphics.Color.Yellow))
        },
        content = {
            Box(Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Blue))
        },
        right = {
            Box(Modifier.width(60.dp).fillMaxHeight().background(androidx.compose.ui.graphics.Color.Green))
        },
        dialog = {
        },
        modifier = Modifier.fillMaxSize(),
    )
}
