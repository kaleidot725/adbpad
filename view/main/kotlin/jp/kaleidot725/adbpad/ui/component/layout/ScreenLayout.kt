package jp.kaleidot725.adbpad.ui.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ScreenLayout(
    top: (@Composable () -> Unit)? = null,
    navigationRail: @Composable () -> Unit,
    content: @Composable () -> Unit,
    right: @Composable () -> Unit,
    dialog: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.background(MaterialTheme.colorScheme.background)) {
        Column {
            if (top != null) {
                top()
            }
            Row(modifier = Modifier.weight(1f)) {
                Box(Modifier.background(MaterialTheme.colorScheme.background)) { navigationRail() }
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .weight(1f)
                        .padding(start = 8.dp, top = 8.dp, end = 12.dp, bottom = 12.dp),
                ) {
                    content()
                }
                Box(Modifier.background(MaterialTheme.colorScheme.background)) { right() }
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
