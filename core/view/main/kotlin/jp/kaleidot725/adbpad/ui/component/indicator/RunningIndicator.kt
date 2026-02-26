package jp.kaleidot725.adbpad.ui.component.indicator

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RunningIndicator(
    color: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = 2.dp,
        modifier = modifier.size(20.dp),
    )
}

@Preview
@Composable
private fun RunningIndicator_Preview() {
    RunningIndicator()
}
