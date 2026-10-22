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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.StatusConfirmedGreen
import com.example.tucancha.ui.theme.StatusPendingYellow
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary
import com.example.tucancha.ui.theme.UnavailableRed

@Composable
fun AdminDashboardScreen(
    reservations: List<Reservation>,
    onUpdateReservationStatus: (reservationId: String, newStatus: ReservationStatus) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilterStatus by remember { mutableStateOf<ReservationStatus?>(null) }

    // Metrics Calculation
    val totalReservas = reservations.size
    val confirmadas = reservations.count { it.status == ReservationStatus.CONFIRMADA }
    val conversionPct = if (totalReservas > 0) (confirmadas * 100 / totalReservas) else 0
    val totalIngresos = reservations.filter { it.status == ReservationStatus.CONFIRMADA }.sumOf { it.totalPrice.toDouble() }
    val canchasActivas = 5

    // Filtering logic
    val filteredReservations = if (selectedFilterStatus == null) {
        reservations
    } else {
        reservations.filter { it.status == selectedFilterStatus }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DeporYellowPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "DeporPlaza",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ADMIN CENTER",
                        color = DeporYellowPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "OPERACIONES",
                        color = DeporYellowPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Panel de reservas",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Resumen de actividad y pagos de DeporPlaza.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // 4 Metric Cards Grid
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            icon = Icons.Default.CalendarMonth,
                            value = totalReservas.toString(),
                            title = "Reservas",
                            subtitle = "Totales",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            icon = Icons.Default.CheckCircle,
                            value = confirmadas.toString(),
                            title = "Confirmadas",
                            subtitle = "$conversionPct% conversión",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            icon = Icons.Default.CreditCard,
                            value = "S/$totalIngresos",
                            title = "Ingresos",
                            subtitle = "Yape, agente y caja",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            icon = Icons.Default.Sports,
                            value = "$canchasActivas",
                            title = "Canchas activas",
                            subtitle = "de 6 canchas",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Reservas recientes",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gestiona los pagos y estados de cada cancha.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            label = "Todas",
                            isSelected = selectedFilterStatus == null,
                            onClick = { selectedFilterStatus = null }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Pendiente",
                            isSelected = selectedFilterStatus == ReservationStatus.PENDIENTE,
                            onClick = { selectedFilterStatus = ReservationStatus.PENDIENTE }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Confirmada",
                            isSelected = selectedFilterStatus == ReservationStatus.CONFIRMADA,
                            onClick = { selectedFilterStatus = ReservationStatus.CONFIRMADA }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Cancelada",
                            isSelected = selectedFilterStatus == ReservationStatus.CANCELADA,
                            onClick = { selectedFilterStatus = ReservationStatus.CANCELADA }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (filteredReservations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay reservas en esta categoría",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredReservations, key = { it.id }) { reservation ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        AdminReservationCard(
                            reservation = reservation,
                            onConfirmPayment = {
                                onUpdateReservationStatus(reservation.id, ReservationStatus.CONFIRMADA)
                            },
                            onRejectPayment = {
                                onUpdateReservationStatus(reservation.id, ReservationStatus.CANCELADA)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    icon: ImageVector,
    value: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, shape)
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF2B2412)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DeporYellowPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = value,
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(if (isSelected) DeporYellowPrimary else DarkSurface)
            .border(1.dp, if (isSelected) DeporYellowPrimary else DarkCardBorder, shape)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun AdminReservationCard(
    reservation: Reservation,
    onConfirmPayment: () -> Unit,
    onRejectPayment: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, shape)
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = reservation.court.imageUrl,
                contentDescription = reservation.court.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = reservation.court.name,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "S/${reservation.totalPrice}",
                        color = DeporYellowPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Badge
                    val statusBg = when (reservation.status) {
                        ReservationStatus.CONFIRMADA -> Color(0xFF1E3A2B)
                        ReservationStatus.PENDIENTE -> Color(0xFF382A00)
                        ReservationStatus.CANCELADA -> Color(0xFF3A1E1E)
                    }
                    val statusText = when (reservation.status) {
                        ReservationStatus.CONFIRMADA -> StatusConfirmedGreen
                        ReservationStatus.PENDIENTE -> StatusPendingYellow
                        ReservationStatus.CANCELADA -> UnavailableRed
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = reservation.status.displayName,
                            color = statusText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = reservation.receiptCode,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${reservation.userName} · ${reservation.id}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                val durationFormatted = if (reservation.durationHours % 1.0 == 0.0) "${reservation.durationHours.toInt()} h" else "${reservation.durationHours} h"
                Text(
                    text = "${reservation.date} · ${reservation.startTime} · $durationFormatted",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                val codeText = if (reservation.operationCode.isNotBlank()) " #${reservation.operationCode}" else ""
                Text(
                    text = "${reservation.paymentMethod.displayName}$codeText",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Action Buttons if Pendiente
        if (reservation.status == ReservationStatus.PENDIENTE) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectPayment,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Rechazar", color = TextSecondary, fontSize = 13.sp)
                }

                Button(
                    onClick = onConfirmPayment,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeporYellowPrimary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Confirmar pago", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
