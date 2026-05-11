package jp.kaleidot725.adbpad.ui.screen.app.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.AppFilePreview
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFilePreviewState

@Composable
fun AppFilePreviewPane(
    state: AppFilePreviewState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppFilePreviewHeader(
            entry = state.entry,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )

        HorizontalDivider(color = UserColor.getSplitterColor())

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.errorMessage != null -> {
                    AppFilePreviewNoImage(
                        details = state.errorMessage,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                state.preview is AppFilePreview.Image -> {
                    AsyncImage(
                        model = state.preview.localFile,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                state.preview is AppFilePreview.Text -> {
                    AppFileTextPreview(
                        text = state.preview.text,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                else -> {
                    AppFilePreviewNoImage(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun AppFilePreviewHeader(
    entry: AppFileEntry?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = Language.appFilePreviewTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (entry != null) {
            Text(
                text = entry.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = entry.path,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AppFileTextPreview(
    text: String,
    modifier: Modifier = Modifier,
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    SelectionContainer {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            modifier =
                modifier
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState),
        )
    }
}

@Composable
private fun AppFilePreviewNoImage(
    details: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = Language.appFilePreviewNoImage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (!details.isNullOrBlank()) {
            Text(
                text = details,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun AppFilePreviewPanePreview() {
    AppFilePreviewPane(
        state =
            AppFilePreviewState(
                entry =
                    AppFileEntry.File(
                        name = "settings.json",
                        path = "/data/data/com.example/files/settings.json",
                        permissions = "-rw-r--r--",
                        size = 128,
                        date = "2026-05-10",
                        time = "12:00",
                    ),
                preview =
                    AppFilePreview.Text(
                        entry =
                            AppFileEntry.File(
                                name = "settings.json",
                                path = "/data/data/com.example/files/settings.json",
                                permissions = "-rw-r--r--",
                                size = 128,
                                date = "2026-05-10",
                                time = "12:00",
                            ),
                        text = "{\n  \"enabled\": true\n}",
                    ),
            ),
        modifier = Modifier.fillMaxSize(),
    )
}
