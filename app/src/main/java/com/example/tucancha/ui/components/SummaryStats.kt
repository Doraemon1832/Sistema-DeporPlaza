package com.example.tucancha.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun SummaryStats(
    totalCourts: Int,
    availableCourts: Int,
    totalSports: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            emoji = "🏟️",
            count = totalCourts.toString(),
            label = "Canchas",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            emoji = "✅",
            count = availableCourts.toString(),
            label = "Disponibles",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            emoji = "🎯",
            count = totalSports.toString(),
            label = "Deportes",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    emoji: String,
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, shape)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            color = DeporYellowPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}
