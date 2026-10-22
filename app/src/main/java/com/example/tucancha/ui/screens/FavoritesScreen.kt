package com.example.tucancha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.model.Court
import com.example.tucancha.ui.components.CourtCard
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun FavoritesScreen(
    courts: List<Court>,
    favoriteCourtIds: Set<String>,
    onFavoriteToggle: (String) -> Unit,
    onReserveClick: (Court) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteCourts = courts.filter { favoriteCourtIds.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Favoritos",
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        if (favoriteCourts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "💔",
                        fontSize = 54.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aún no tienes favoritos",
                        color = TextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(favoriteCourts, key = { it.id }) { court ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        CourtCard(
                            court = court,
                            isFavorite = true,
                            onFavoriteToggle = { onFavoriteToggle(court.id) },
                            onReserveClick = { onReserveClick(court) }
                        )
                    }
                }
            }
        }
    }
}
