package org.example.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.ui.ProgrammingLanguageScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
  MaterialTheme {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
      Column {
        val platform = getPlatform()
        Text("Hello,  ${platform.name}!", modifier = Modifier.padding(16.dp))
        ProgrammingLanguageScreen(
          modifier = Modifier.padding(innerPadding),
        )
      }
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App()
}
