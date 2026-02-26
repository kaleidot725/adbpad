package jp.kaleidot725.adbpad.ui.common.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.ui.common.resource.UserColor.getSplitterColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modifier.clickableBackground(
    isSelected: Boolean = false,
    selectedColor: Color = UserColor.getContentBackgroundColor(),
    shape: Shape = RectangleShape,
    isDarker: Boolean = false,
): Modifier {
    var isMouseOver by remember { mutableStateOf(false) }
    val one =
        if (isSelected) {
            this.background(color = selectedColor, shape = shape)
        } else if (isMouseOver) {
            this.background(
                color =
                    if (isDarker) {
                        Color.White.copy(alpha = 0.1f)
                    } else if (MaterialTheme.colorScheme.surface.luminance() > 0.5) {
                        Color.DarkGray.copy(alpha = 0.1f)
                    } else {
                        Color.White.copy(alpha = 0.1f)
                    },
                shape = shape,
            )
        } else {
            this
        }
    val two = one.onPointerEvent(PointerEventType.Enter) { isMouseOver = true }
    val three = two.onPointerEvent(PointerEventType.Exit) { isMouseOver = false }
    return three
}

@Composable
fun Modifier.defaultBorder(): Modifier = this.border(shape = RoundedCornerShape(8.dp), color = getSplitterColor(), width = 1.dp)
