package org.example.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.data.ProgrammingLanguage

@Composable
fun ProgrammingLanguageScreen(
  modifier: Modifier = Modifier,
  viewModel: ProgrammingLanguageViewModel = viewModel(),
) {
  val programmingLanguages by viewModel.programmingLanguages.collectAsStateWithLifecycle()
  val selectedLanguages by viewModel.selectedLanguages.collectAsStateWithLifecycle()

  // Bottom sheet state
  var showBottomSheet by remember { mutableStateOf(false) }
  var remainingItems by remember { mutableStateOf<List<ProgrammingLanguage>>(emptyList()) }

  Column(
    modifier =
      modifier
        .fillMaxSize()
        .padding(16.dp),
  ) {
    ContextualFlowRow(
      items = programmingLanguages,
      modifier = Modifier.fillMaxWidth(),
      maxLines = 2,
      horizontalSpacing = 8.dp,
      verticalSpacing = 8.dp,
      onMoreClick = { remaining ->
        remainingItems = remaining
        showBottomSheet = true
      },
    ) { language ->
      ProgrammingLanguageChip(
        language = language,
        isSelected = selectedLanguages.contains(language.id),
        onLanguageClick = { viewModel.onLanguageSelected(language.id) },
      )
    }

    // Bottom sheet for remaining items
    RemainingItemsModalBottomSheet(
      items = remainingItems,
      isVisible = showBottomSheet,
      onDismiss = { showBottomSheet = false },
    ) { language ->
      ProgrammingLanguageChip(
        language = language,
        isSelected = selectedLanguages.contains(language.id),
        onLanguageClick = { viewModel.onLanguageSelected(language.id) },
      )
    }
  }
}

@Composable
fun ProgrammingLanguageChip(
  language: ProgrammingLanguage,
  isSelected: Boolean,
  onLanguageClick: () -> Unit,
) {
  FilterChip(
    onClick = onLanguageClick,
    label = { Text(language.name) },
    selected = isSelected,
    colors =
      FilterChipDefaults.filterChipColors(
        containerColor = Color(0xFF5A6B8C),
        labelColor = Color.White,
        selectedContainerColor = Color(0xFF4A5B7C),
        selectedLabelColor = Color.White,
      ),
    shape = RoundedCornerShape(20.dp),
  )
}
