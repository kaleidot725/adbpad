package jp.kaleidot725.adbpad.ui.component.button

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground

@Composable
fun CommandTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    padding: Dp = 0.dp,
) {
    Box(
        modifier =
            modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickableBackground(isDarker = true)
                .clickable { onClick() }
                .padding(padding),
    ) {
        Text(
            text = text,
            fontSize = 8.sp,
            modifier = Modifier.align(Alignment.Center).padding(bottom = 8.dp),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommandTextButton(
        text = "100%",
        onClick = {},
    )
}
