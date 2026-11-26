package com.example.tucancha.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.UserProfile
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.StarYellow
import com.example.tucancha.ui.theme.StatusConfirmedGreen
import com.example.tucancha.ui.theme.StatusPendingYellow
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary
import com.example.tucancha.ui.theme.UnavailableRed

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    reservations: List<Reservation>,
    onCancelReservation: (String) -> Unit,
    onUpdateProfile: (newNombre: String, newEmail: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onDeleteAccount: (onResult: (Boolean) -> Unit) -> Unit,
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var reservationToCancelId by remember { mutableStateOf<String?>(null) }
    var isEditProfileOpen by remember { mutableStateOf(false) }
    var isDeleteAccountOpen by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(userProfile.name) }
    var editEmail by remember { mutableStateOf(userProfile.email) }
    var editErrorMessage by remember { mutableStateOf<String?>(null) }

    // Filtrar únicamente las reservas del usuario actualmente conectado
    val userReservations = reservations.filter {
        it.userName.equals(userProfile.name, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

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

        // User Profile Card
        val profileShape = RoundedCornerShape(20.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(profileShape)
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, profileShape)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(DeporYellowPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.avatarLetter,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.name,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userProfile.email,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userProfile.badge,
                            color = DeporYellowPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = {
                        editName = userProfile.name
                        editEmail = userProfile.email
                        editErrorMessage = null
                        isEditProfileOpen = true
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262B30))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar perfil",
                        tint = DeporYellowPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Eliminar mi cuenta Button
            OutlinedButton(
                onClick = { isDeleteAccountOpen = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = UnavailableRed),
                border = BorderStroke(1.dp, UnavailableRed.copy(alpha = 0.5f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = UnavailableRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Eliminar mi cuenta",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnavailableRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section Title: MIS RESERVAS (X)
        Text(
            text = "MIS RESERVAS (${userReservations.size})",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (userReservations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF252A30)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFA5B4FC),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No tienes reservas aún",
                        color = TextSecondary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(userReservations, key = { it.id }) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onCancelClick = { reservationToCancelId = reservation.id }
                    )
                }
            }
        }
    }

    // Edit Profile Dialog Modal
    if (isEditProfileOpen) {
        AlertDialog(
            onDismissRequest = { isEditProfileOpen = false },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = "Editar perfil",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    editErrorMessage?.let { err ->
                        Text(
                            text = err,
                            color = UnavailableRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(text = "Nombre completo", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it; editErrorMessage = null },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeporYellowPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = Color(0xFF121518),
                            unfocusedContainerColor = Color(0xFF121518),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Correo electrónico", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it; editErrorMessage = null },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeporYellowPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = Color(0xFF121518),
                            unfocusedContainerColor = Color(0xFF121518),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editName, editEmail) { exito, msg ->
                            if (exito) {
                                isEditProfileOpen = false
                            } else {
                                editErrorMessage = msg
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeporYellowPrimary, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Guardar cambios", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditProfileOpen = false }) {
                    Text(text = "Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Delete Account Confirmation Dialog Modal
    if (isDeleteAccountOpen) {
        AlertDialog(
            onDismissRequest = { isDeleteAccountOpen = false },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = "⚠️ ¿Eliminar cuenta de DeporPlaza?",
                    color = UnavailableRed,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Esta acción eliminará permanentemente tu usuario y todas tus reservas asociadas de MySQL Workbench. ¿Deseas continuar?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteAccount { exito ->
                            isDeleteAccountOpen = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnavailableRed, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Sí, eliminar cuenta", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { isDeleteAccountOpen = false }) {
                    Text(text = "Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Cancel Reservation Confirmation Dialog
    if (reservationToCancelId != null) {
        AlertDialog(
            onDismissRequest = { reservationToCancelId = null },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = "Cancelar reserva",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar esta reserva de cancha?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        reservationToCancelId?.let { onCancelReservation(it) }
                        reservationToCancelId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnavailableRed, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Sí, cancelar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { reservationToCancelId = null }) {
                    Text(text = "Volver", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ReservationCard(
    reservation: Reservation,
    onCancelClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(cardShape)
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, cardShape)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = reservation.court.imageUrl,
            contentDescription = reservation.court.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
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

                // Status Badge
                val isConfirmed = reservation.status == ReservationStatus.CONFIRMADA
                val statusBg = if (isConfirmed) Color(0xFF1E3A2B) else Color(0xFF382A00)
                val statusText = if (isConfirmed) StatusConfirmedGreen else StatusPendingYellow

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = reservation.status.displayName,
                        color = statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            val durationFormatted = if (reservation.durationHours % 1.0 == 0.0) {
                "${reservation.durationHours.toInt()}h"
            } else {
                "${reservation.durationHours}h"
            }

            Text(
                text = "${reservation.date} · ${reservation.startTime} · $durationFormatted",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/${reservation.totalPrice}",
                    color = DeporYellowPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onCancelClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancelar reserva",
                        tint = UnavailableRed.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
