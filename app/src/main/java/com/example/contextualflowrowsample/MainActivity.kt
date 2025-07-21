package com.example.contextualflowrowsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contextualflowrowsample.ui.ProgrammingLanguageScreen
import com.example.contextualflowrowsample.ui.theme.ContextualFlowRowSampleTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ContextualFlowRowSampleTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          ProgrammingLanguageScreen(
            modifier = Modifier.padding(innerPadding),
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ProgrammingLanguageScreenPreview() {
  ContextualFlowRowSampleTheme {
    // For preview, we'll use a mock ViewModel
    // In real app, Hilt will provide the ViewModel
    ProgrammingLanguageScreen()
  }
}
