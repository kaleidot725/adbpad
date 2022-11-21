package jp.kaleidot725.adbpad.view.component.setting

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingTitle(text: String, modifier: Modifier = Modifier) {
    Text(text = text, style = MaterialTheme.typography.h6)
}

@Preview
@Composable
private fun SettingTitle_Preview() {
    SettingTitle("ADB ＆ Android SDK")
}