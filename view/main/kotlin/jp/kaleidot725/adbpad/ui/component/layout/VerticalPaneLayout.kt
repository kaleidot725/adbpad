package jp.kaleidot725.adbpad.ui.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.VerticalSplitPane
import java.awt.Cursor

fun Modifier.cursorForVerticalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun VerticalPaneLayout(
    splitterState: SplitPaneState,
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topMinSize: Dp = 100.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    VerticalSplitPane(
        splitPaneState = splitterState,
        modifier = modifier.fillMaxSize(),
    ) {
        first(minSize = topMinSize) {
            Box(modifier = Modifier.fillMaxSize().padding(bottom = 4.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    top()
                }
            }
        }

        second {
            Box(modifier = Modifier.fillMaxSize().padding(top = 4.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    bottom()
                }
            }
        }

        splitter {
            visiblePart { }
            handle {
                Box(
                    Modifier
                        .markAsHandle()
                        .cursorForVerticalResize()
                        .hoverable(interactionSource)
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            if (isHovered) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            } else {
                                Color.Transparent
                            },
                        ),
                )
            }
        }
    }
}
