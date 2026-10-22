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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tucancha.model.Court
import com.example.tucancha.model.DayOption
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary
import com.example.tucancha.ui.theme.UnavailableButtonBackground
import com.example.tucancha.ui.theme.UnavailableButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    court: Court,
    selectedDay: DayOption,
    selectedTimeSlot: String,
    selectedDuration: Double,
    totalPrice: Int,
    onConfirmPayment: (PaymentMethod, operationCode: String, phoneNumber: String) -> Unit,
    onBackToTimeSelection: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedMethod by remember { mutableStateOf(PaymentMethod.YAPE) }
    var phoneNumber by remember { mutableStateOf("987654321") }
    var operationCode by remember { mutableStateOf("") }

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
            // Header: PASO 2 DE 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PASO 2 DE 2",
                        color = DeporYellowPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Elige cómo pagar",
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

            // Summary Card
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
                    text = "S/$totalPrice",
                    color = DeporYellowPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MÉTODO DE PAGO Title
            Text(
                text = "MÉTODO DE PAGO",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 3 Payment Method Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PaymentMethodChip(
                    method = PaymentMethod.YAPE,
                    shortCode = "Y",
                    title = "Yape",
                    isSelected = selectedMethod == PaymentMethod.YAPE,
                    onClick = { selectedMethod = PaymentMethod.YAPE },
                    modifier = Modifier.weight(1f)
                )
                PaymentMethodChip(
                    method = PaymentMethod.AGENTE,
                    shortCode = "AB",
                    title = "Agente bancario",
                    isSelected = selectedMethod == PaymentMethod.AGENTE,
                    onClick = { selectedMethod = PaymentMethod.AGENTE },
                    modifier = Modifier.weight(1f)
                )
                PaymentMethodChip(
                    method = PaymentMethod.EFECTIVO,
                    shortCode = "S/",
                    title = "Efectivo en caja",
                    isSelected = selectedMethod == PaymentMethod.EFECTIVO,
                    onClick = { selectedMethod = PaymentMethod.EFECTIVO },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dynamic Content per Selected Method
            when (selectedMethod) {
                PaymentMethod.YAPE -> {
                    // Purple Yape Container
                    val yapeShape = RoundedCornerShape(16.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(yapeShape)
                            .background(Color(0xFF23182C))
                            .border(1.dp, Color(0xFF4A2B60), yapeShape)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF6B21A8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Y",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Pago seguro con Yape",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Escánea o yapea al 987 000 321",
                                    color = Color(0xFFD8B4FE),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // QR Box inside Yape
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF581C87))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // QR Icon Container
                                Box(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = "QR Code Yape",
                                        tint = Color.Black,
                                        modifier = Modifier.size(90.dp)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "MONTO EXACTO",
                                        color = Color(0xFFE9D5FF),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "S/$totalPrice",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "DeporPlaza Perú",
                                        color = Color(0xFFE9D5FF),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "NÚMERO DE CELULAR",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeporYellowPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = Color(0xFF121518),
                            unfocusedContainerColor = Color(0xFF121518)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "CÓDIGO DE OPERACIÓN",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = operationCode,
                        onValueChange = { operationCode = it },
                        placeholder = { Text(text = "Ej. 729104", color = TextSecondary) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeporYellowPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = Color(0xFF121518),
                            unfocusedContainerColor = Color(0xFF121518)
                        )
                    )
                }

                PaymentMethod.AGENTE -> {
                    val agenteShape = RoundedCornerShape(16.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(agenteShape)
                            .background(Color(0xFF1D2218))
                            .border(1.dp, Color(0xFF384A22), agenteShape)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeporYellowPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AB",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Depósito en agente bancario",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "BCP · Cuenta 191-2456789-0-12",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monto a depositar",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "S/$totalPrice",
                                color = DeporYellowPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CÓDIGO DE OPERACIÓN",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = operationCode,
                        onValueChange = { operationCode = it },
                        placeholder = { Text(text = "Ej. 729104", color = TextSecondary) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeporYellowPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = Color(0xFF121518),
                            unfocusedContainerColor = Color(0xFF121518)
                        )
                    )
                }

                PaymentMethod.EFECTIVO -> {
                    val efectivoShape = RoundedCornerShape(16.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(efectivoShape)
                            .background(Color(0xFF1E2219))
                            .border(1.dp, DeporYellowPrimary.copy(alpha = 0.5f), efectivoShape)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Paga al llegar",
                            color = DeporYellowPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Acércate a caja 30 minutos antes de tu horario. Tu reserva quedará pendiente hasta que realices el pago en efectivo.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Esta es una simulación. No se realizará ningún cobro real.",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            val isButtonEnabled = selectedMethod == PaymentMethod.EFECTIVO || operationCode.isNotBlank()
            Button(
                onClick = {
                    onConfirmPayment(
                        selectedMethod,
                        operationCode.ifBlank { "SIN_CODIGO" },
                        phoneNumber
                    )
                },
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedMethod == PaymentMethod.EFECTIVO) "Reservar y pagar en caja" else "Confirmar pago",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBackToTimeSelection() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Volver al horario",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodChip(
    method: PaymentMethod,
    shortCode: String,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipShape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(chipShape)
            .background(if (isSelected) Color(0xFF262113) else Color(0xFF1E2328))
            .border(
                width = 1.dp,
                color = if (isSelected) DeporYellowPrimary else DarkCardBorder,
                shape = chipShape
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = shortCode,
            color = if (isSelected) DeporYellowPrimary else TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = if (isSelected) DeporYellowPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
