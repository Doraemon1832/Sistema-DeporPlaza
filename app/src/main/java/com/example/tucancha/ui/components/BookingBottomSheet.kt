package com.example.tucancha.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tucancha.data.MockData
import com.example.tucancha.model.Court
import com.example.tucancha.model.DayOption
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary
import com.example.tucancha.ui.theme.UnavailableButtonBackground
import com.example.tucancha.ui.theme.UnavailableButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomSheet(
    court: Court,
    existingReservations: List<Reservation>,
    selectedDay: DayOption,
    onDaySelected: (DayOption) -> Unit,
    selectedTimeSlot: String?,
    onTimeSlotSelected: (String) -> Unit,
    selectedDuration: Double,
    onDurationSelected: (Double) -> Unit,
    onContinueToPaymentClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Calculate taken slots for selected court & day
    val occupiedTimeSlots = existingReservations
        .filter { it.court.id == court.id && it.date == selectedDay.fullDate && it.status != ReservationStatus.CANCELADA }
        .map { it.startTime }
        .toSet()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = DarkSurface,
        scrimColor = Color.Black.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PASO 1 DE 2",
                        color = DeporYellowPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Elige tu horario",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF262B30))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Court Summary Card
            val summaryShape = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(summaryShape)
                    .background(Color(0xFF13171A))
                    .border(1.dp, DarkCardBorder, summaryShape)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = court.imageUrl,
                    contentDescription = court.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = court.name,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = court.location,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = "S/${court.pricePerHour}",
                    color = DeporYellowPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SELECCIONA EL DÍA
            Text(
                text = "SELECCIONA EL DÍA",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MockData.sampleDays) { day ->
                    val isSelected = day.fullDate == selectedDay.fullDate
                    val shape = RoundedCornerShape(14.dp)

                    Column(
                        modifier = Modifier
                            .clip(shape)
                            .background(if (isSelected) DeporYellowPrimary else Color(0xFF1E2328))
                            .border(1.dp, if (isSelected) DeporYellowPrimary else DarkCardBorder, shape)
                            .clickable { onDaySelected(day) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayOfWeek,
                            color = if (isSelected) Color.Black else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = day.dayNumber,
                            color = if (isSelected) Color.Black else TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // HORA DE INICIO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HORA DE INICIO",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                if (occupiedTimeSlots.isNotEmpty()) {
                    Text(
                        text = "🔒 ${occupiedTimeSlots.size} turno(s) ocupado(s)",
                        color = Color(0xFFFFB74D),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MockData.sampleTimeSlots) { time ->
                    val isOccupied = occupiedTimeSlots.contains(time)
                    val isSelected = time == selectedTimeSlot && !isOccupied
                    val shape = RoundedCornerShape(10.dp)

                    val bgColor = when {
                        isOccupied -> Color(0xFF2C2424)
                        isSelected -> DeporYellowPrimary
                        else -> Color(0xFF1E2328)
                    }

                    val borderColor = when {
                        isOccupied -> Color(0xFF5A2A2A)
                        isSelected -> DeporYellowPrimary
                        else -> DarkCardBorder
                    }

                    Box(
                        modifier = Modifier
                            .clip(shape)
                            .background(bgColor)
                            .border(1.dp, borderColor, shape)
                            .clickable(enabled = !isOccupied) { onTimeSlotSelected(time) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = time,
                                color = when {
                                    isOccupied -> Color(0xFF886666)
                                    isSelected -> Color.Black
                                    else -> TextPrimary
                                },
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textDecoration = if (isOccupied) TextDecoration.LineThrough else TextDecoration.None
                            )
                            if (isOccupied) {
                                Text(
                                    text = "Ocupado",
                                    color = Color(0xFFEF5350),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DURACIÓN
            Text(
                text = "DURACIÓN",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MockData.sampleDurations.forEach { (durationValue, durationLabel) ->
                    val isSelected = durationValue == selectedDuration
                    val shape = RoundedCornerShape(12.dp)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape)
                            .background(if (isSelected) DeporYellowPrimary else Color(0xFF1E2328))
                            .border(1.dp, if (isSelected) DeporYellowPrimary else DarkCardBorder, shape)
                            .clickable { onDurationSelected(durationValue) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = durationLabel,
                            color = if (isSelected) Color.Black else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Summary Card Box
            val boxShape = RoundedCornerShape(16.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(boxShape)
                    .background(Color(0xFF13171A))
                    .border(1.dp, DarkCardBorder, boxShape)
                    .padding(16.dp)
            ) {
                val totalPrice = (court.pricePerHour * selectedDuration).toInt()
                val durationText = if (selectedDuration % 1.0 == 0.0) "${selectedDuration.toInt()} h" else "$selectedDuration h"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Fecha y hora",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${selectedDay.fullDate} · ${selectedTimeSlot ?: "—"}",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "S/$totalPrice",
                        color = DeporYellowPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$durationText × S/${court.pricePerHour}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Button Continuar al pago
            val isButtonEnabled = selectedTimeSlot != null && !occupiedTimeSlots.contains(selectedTimeSlot)
            Button(
                onClick = onContinueToPaymentClick,
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeporYellowPrimary,
                    contentColor = Color.Black,
                    disabledContainerColor = UnavailableButtonBackground,
                    disabledContentColor = UnavailableButtonText
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Continuar al pago",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
