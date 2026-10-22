package com.example.tucancha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.model.Court
import com.example.tucancha.model.SportCategory
import com.example.tucancha.ui.components.CourtCard
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    courts: List<Court>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: SportCategory,
    onCategorySelected: (SportCategory) -> Unit,
    favoriteCourtIds: Set<String>,
    onFavoriteToggle: (String) -> Unit,
    onReserveClick: (Court) -> Unit,
    userName: String = "Alejandro",
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var maxPriceFilter by remember { mutableFloatStateOf(150f) }

    // Filtering logic
    val filteredCourts = courts.filter { court ->
        val matchesCategory = selectedCategory == SportCategory.TODOS || court.sport == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                court.name.contains(searchQuery, ignoreCase = true) ||
                court.location.contains(searchQuery, ignoreCase = true)
        val matchesPrice = court.pricePerHour <= maxPriceFilter.toInt()

        matchesCategory && matchesSearch && matchesPrice
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Bienvenido, $userName",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Encuentra tu cancha",
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF262B30))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Search Bar
            item {
                val searchShape = RoundedCornerShape(16.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(searchShape)
                        .background(DarkSurface)
                        .border(1.dp, DarkCardBorder, searchShape)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
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
                            .background(Color(0xFF262B30))
                            .clickable { showFilterDialog = true }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Sport Category Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(SportCategory.entries.toTypedArray()) { category ->
                        val isSelected = category == selectedCategory
                        val chipShape = RoundedCornerShape(14.dp)

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
                                .padding(horizontal = 22.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.displayName,
                                color = if (isSelected) Color.Black else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredCourts.size} CANCHAS",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Lima, Perú",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (filteredCourts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron canchas",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Intenta cambiar los filtros o búsqueda",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(filteredCourts, key = { it.id }) { court ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        CourtCard(
                            court = court,
                            isFavorite = favoriteCourtIds.contains(court.id),
                            onFavoriteToggle = { onFavoriteToggle(court.id) },
                            onReserveClick = { onReserveClick(court) }
                        )
                    }
                }
            }
        }

        // Quick Filter Dialog
        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                containerColor = DarkSurface,
                title = {
                    Text(
                        text = "Filtrar por Precio Máximo",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Hasta: S/${maxPriceFilter.toInt()}/hr",
                            color = DeporYellowPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = maxPriceFilter,
                            onValueChange = { maxPriceFilter = it },
                            valueRange = 50f..200f,
                            steps = 15,
                            colors = SliderDefaults.colors(
                                thumbColor = DeporYellowPrimary,
                                activeTrackColor = DeporYellowPrimary,
                                inactiveTrackColor = TextSecondary.copy(alpha = 0.3f)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showFilterDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = DeporYellowPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Aplicar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        maxPriceFilter = 200f
                        showFilterDialog = false
                    }) {
                        Text(text = "Restablecer", color = TextSecondary)
                    }
                }
            )
        }
    }
}
