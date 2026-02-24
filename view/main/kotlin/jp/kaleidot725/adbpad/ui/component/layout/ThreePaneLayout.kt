package jp.kaleidot725.adbpad.ui.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun ThreePaneLayout(
    splitterState: SplitPaneState,
    rightSplitterState: SplitPaneState,
    left: @Composable () -> Unit,
    center: (@Composable () -> Unit)? = null,
    right: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    leftMinSize: Dp = 200.dp,
    rightMinSize: Dp = 200.dp,
) {
    val leftInteractionSource = remember { MutableInteractionSource() }
    val leftIsHovered by leftInteractionSource.collectIsHoveredAsState()
    val rightInteractionSource = remember { MutableInteractionSource() }
    val rightIsHovered by rightInteractionSource.collectIsHoveredAsState()

    HorizontalSplitPane(
        splitPaneState = splitterState,
        modifier = modifier.fillMaxSize(),
    ) {
        first(minSize = leftMinSize) {
            Box(modifier = Modifier.fillMaxSize().padding(end = 4.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    left()
                }
            }
        }

        second {
            Box(modifier = Modifier.fillMaxSize().padding(start = 4.dp)) {
                when {
                    center != null && right != null -> {
                        HorizontalSplitPane(
                            splitPaneState = rightSplitterState,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            first {
                                Box(modifier = Modifier.fillMaxSize().padding(end = 4.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        center()
                                    }
                                }
                            }
                            second(minSize = rightMinSize) {
                                Box(modifier = Modifier.fillMaxSize().padding(start = 4.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        right()
                                    }
                                }
                            }
                            splitter {
                                visiblePart { }
                                handle {
                                    Box(
                                        Modifier
                                            .markAsHandle()
                                            .cursorForHorizontalResize()
                                            .hoverable(rightInteractionSource)
                                            .width(8.dp)
                                            .fillMaxHeight()
                                            .background(
                                                if (rightIsHovered) {
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
                    center != null -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            center()
                        }
                    }
                    right != null -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            right()
                        }
                    }
                }
            }
        }

        splitter {
            visiblePart { }
            handle {
                Box(
                    Modifier
                        .markAsHandle()
                        .cursorForHorizontalResize()
                        .hoverable(leftInteractionSource)
                        .width(8.dp)
                        .fillMaxHeight()
                        .background(
                            if (leftIsHovered) {
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
