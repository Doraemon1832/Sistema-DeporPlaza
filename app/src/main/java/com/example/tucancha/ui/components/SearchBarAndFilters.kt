package com.example.tucancha.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.model.SportCategory
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun SearchBarAndFilters(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: SportCategory,
    onCategorySelected: (SportCategory) -> Unit,
    onFilterIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val searchShape = RoundedCornerShape(16.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(searchShape)
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, searchShape)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Busca por cancha o distrito",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(DeporYellowPrimary),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeporYellowPrimary)
                    .clickable { onFilterIconClick() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtros",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sport Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(SportCategory.entries.toTypedArray()) { category ->
                val isSelected = category == selectedCategory
                val chipShape = CircleShape

                Box(
                    modifier = Modifier
                        .clip(chipShape)
                        .background(if (isSelected) DeporYellowPrimary else DarkSurface)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) DeporYellowPrimary else DarkCardBorder,
                            shape = chipShape
                        )
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.displayName,
                        color = if (isSelected) Color.Black else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
