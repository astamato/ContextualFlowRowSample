package com.example.contextualflowrowsample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RemainingItemsModalBottomSheet(
  items: List<T>,
  isVisible: Boolean,
  onDismiss: () -> Unit,
  itemContent: @Composable (T) -> Unit,
) {
  if (isVisible) {
    ModalBottomSheet(
      onDismissRequest = onDismiss,
      sheetState = rememberModalBottomSheetState(),
    ) {
      RemainingItemsModalBottomSheetContent(
        items = items,
        onDismiss = onDismiss,
        itemContent = itemContent,
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> RemainingItemsModalBottomSheetContent(
  items: List<T>,
  onDismiss: () -> Unit,
  itemContent: @Composable (T) -> Unit,
) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(16.dp),
  ) {
    Text(
      text = "${items.size} More Items",
      style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(bottom = 16.dp),
    )

    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items.forEach { item ->
        itemContent(item)
      }
    }

    // Add some bottom padding for the bottom sheet
    Spacer(
      modifier = Modifier.padding(bottom = 32.dp),
    )
  }
}
