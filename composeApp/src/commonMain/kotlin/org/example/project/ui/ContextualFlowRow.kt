package org.example.project.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ContextualFlowRow(
  items: List<T>,
  modifier: Modifier = Modifier,
  maxLines: Int = Int.MAX_VALUE,
  horizontalSpacing: Dp = 8.dp,
  verticalSpacing: Dp = 8.dp,
  onMoreClick: (List<T>) -> Unit = {},
  itemContent: @Composable (T) -> Unit,
) {
  val context = LocalDensity.current
  val visibleItemsCountRef = remember { mutableIntStateOf(items.size) }

  Layout(
    modifier = modifier,
    content = {
      // Render ALL items - we'll hide excess ones during layout
      items.forEach { item ->
        itemContent(item)
      }

      // Template "more" chip for measurement only
      AssistChip(
        onClick = { },
        label = { Text("99+ more") },
        colors =
          AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFF5A6B8C),
            labelColor = Color.White,
          ),
        shape = RoundedCornerShape(20.dp),
      )

      // Actual "more" chip that will be shown if needed
      AssistChip(
        onClick = {
          // Calculate remaining items using the stored visible count
          val actualRemainingItems =
            if (visibleItemsCountRef.intValue < items.size) {
              items.drop(visibleItemsCountRef.intValue)
            } else {
              emptyList()
            }
          onMoreClick(actualRemainingItems)
        },
        label = {
          val remainingCount = maxOf(0, items.size - visibleItemsCountRef.intValue)
          Text("$remainingCount+ more")
        },
        colors =
          AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFF5A6B8C),
            labelColor = Color.White,
          ),
        shape = RoundedCornerShape(20.dp),
      )
    },
  ) { measurables, constraints ->
    if (measurables.isEmpty() || items.isEmpty()) {
      return@Layout layout(0, 0) {}
    }

    val spacing = with(context) { horizontalSpacing.roundToPx() }
    val verticalSpacingPx = with(context) { verticalSpacing.roundToPx() }

    // Last two measurables are the template and actual "more" chip
    val templateMeasurable = measurables[measurables.size - 2]
    val actualMoreChipMeasurable = measurables.last()
    val templatePlaceable = templateMeasurable.measure(Constraints())
    val actualMoreChipPlaceable = actualMoreChipMeasurable.measure(Constraints())

    // Measure all item measurables once
    val itemMeasurables = measurables.dropLast(2) // Drop both more chips
    val itemPlaceables = itemMeasurables.map { it.measure(Constraints()) }

    // Calculate visible items
    val result =
      calculateLayout(
        items = items,
        placeables = itemPlaceables,
        moreChipWidth = templatePlaceable.width,
        maxWidth = constraints.maxWidth,
        maxLines = maxLines,
        spacing = spacing,
        verticalSpacing = verticalSpacingPx,
      )

    // If we have remaining items, we need to add the "more" chip
    val finalPlaceables = mutableListOf<Pair<Placeable, Pair<Int, Int>>>()
    finalPlaceables.addAll(result.itemPositions)

    // Store visible count for click handler (using ref to avoid recomposition)
    visibleItemsCountRef.intValue = result.itemPositions.size

    result.moreChipData?.let { (position, count) ->
      // Place the actual "more" chip with correct count and click handler
      finalPlaceables.add(actualMoreChipPlaceable to position)
    }

    layout(result.totalWidth, result.totalHeight) {
      finalPlaceables.forEach { (placeable, position) ->
        placeable.placeRelative(position.first, position.second)
      }
    }
  }
}

private data class LayoutResult(
  val itemPositions: List<Pair<Placeable, Pair<Int, Int>>>,
  val moreChipData: Pair<Pair<Int, Int>, Int>?, // position, count
  val totalWidth: Int,
  val totalHeight: Int,
)

private fun <T> calculateLayout(
  items: List<T>,
  placeables: List<Placeable>,
  moreChipWidth: Int,
  maxWidth: Int,
  maxLines: Int,
  spacing: Int,
  verticalSpacing: Int,
): LayoutResult {
  if (items.isEmpty() || placeables.isEmpty()) {
    return LayoutResult(emptyList(), null, 0, 0)
  }

  val itemPositions = mutableListOf<Pair<Placeable, Pair<Int, Int>>>()
  var currentX = 0
  var currentY = 0
  var currentLine = 1
  var lineHeight = 0
  var maxWidthUsed = 0

  for (i in placeables.indices) {
    if (i >= items.size) break

    val placeable = placeables[i]
    val remainingAfterThis = items.size - i - 1

    // Check if we need to wrap to next line
    if (currentX + placeable.width > maxWidth && currentX > 0) {
      if (currentLine >= maxLines) {
        // Can't add more lines
        break
      }

      currentLine++
      currentX = 0
      currentY += lineHeight + verticalSpacing
      lineHeight = 0
    }

    // If we're on the last line and have remaining items,
    // check if we can fit this item + "more" chip
    if (currentLine == maxLines && remainingAfterThis > 0) {
      val spaceWithMore = currentX + placeable.width + spacing + moreChipWidth
      if (spaceWithMore > maxWidth) {
        // Can't fit this item + more chip
        val remainingCount = items.size - i
        val moreChipPosition = currentX to currentY

        return LayoutResult(
          itemPositions = itemPositions,
          moreChipData = moreChipPosition to remainingCount,
          totalWidth = kotlin.math.max(maxWidthUsed, currentX + moreChipWidth),
          totalHeight = currentY + lineHeight,
        )
      }
    }

    // Place this item
    itemPositions.add(placeable to (currentX to currentY))
    lineHeight = kotlin.math.max(lineHeight, placeable.height)
    currentX += placeable.width + spacing
    maxWidthUsed = kotlin.math.max(maxWidthUsed, currentX - spacing)
  }

  val totalRemainingCount = items.size - itemPositions.size
  val moreChipData =
    if (totalRemainingCount > 0 && itemPositions.isNotEmpty()) {
      // Place more chip after last item
      val lastPosition = itemPositions.last().second
      (lastPosition.first + spacing to lastPosition.second) to totalRemainingCount
    } else {
      null
    }

  val finalHeight =
    if (itemPositions.isNotEmpty()) {
      currentY + lineHeight
    } else {
      0
    }

  return LayoutResult(
    itemPositions = itemPositions,
    moreChipData = moreChipData,
    totalWidth = maxWidthUsed,
    totalHeight = finalHeight,
  )
}
