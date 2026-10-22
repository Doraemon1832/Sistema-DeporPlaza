package com.example.tucancha.ui.components

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun ReceiptDialog(
    reservation: Reservation,
    onBackToHomeClick: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onBackToHomeClick) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkSurface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Check Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262113))
                        .border(2.dp, DeporYellowPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirmado",
                        tint = DeporYellowPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (reservation.status == ReservationStatus.CONFIRMADA) "PAGO VERIFICADO" else "RESERVA REGISTRADA",
                    color = DeporYellowPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (reservation.status == ReservationStatus.CONFIRMADA) "Reserva confirmada" else "Reserva pendiente de pago",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Tu comprobante ya está disponible.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Digital Ticket Card (White Background)
                val ticketShape = RoundedCornerShape(20.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ticketShape)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    // Header of Ticket
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeporYellowPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "DeporPlaza",
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "COMPROBANTE DIGITAL",
                                    color = Color.Gray,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "RECIBO",
                                color = Color.Gray,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = reservation.receiptCode,
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Court details inside ticket
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = reservation.court.imageUrl,
                            contentDescription = reservation.court.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = reservation.court.name,
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = reservation.court.location,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Status Tag
                            val isConfirmed = reservation.status == ReservationStatus.CONFIRMADA
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isConfirmed) Color(0xFFFEF3C7) else Color(0xFFFEF3C7))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = reservation.status.displayName.uppercase(),
                                    color = Color(0xFFB45309),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TicketRow(label = "Cliente", value = reservation.userName)
                    Spacer(modifier = Modifier.height(6.dp))
                    TicketRow(label = "Fecha", value = reservation.date)
                    Spacer(modifier = Modifier.height(6.dp))
                    val durationFormatted = if (reservation.durationHours % 1.0 == 0.0) "${reservation.durationHours.toInt()} h" else "${reservation.durationHours} h"
                    TicketRow(label = "Horario", value = "${reservation.startTime} · $durationFormatted")
                    Spacer(modifier = Modifier.height(6.dp))

                    val paymentText = if (reservation.operationCode.isNotBlank()) {
                        "${reservation.paymentMethod.displayName} · ${reservation.operationCode}"
                    } else {
                        reservation.paymentMethod.displayName
                    }
                    TicketRow(label = "Pago", value = paymentText)

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // QR Code & Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (reservation.status == ReservationStatus.CONFIRMADA) "TOTAL PAGADO" else "TOTAL POR PAGAR",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/${reservation.totalPrice}",
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (reservation.status == ReservationStatus.CONFIRMADA) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "QR Code",
                                tint = Color.Black,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF3F4F6))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Reserva ${reservation.id} · Gracias por elegir DeporPlaza",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { imprimirReciboHTML(context, reservation) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Descargar recibo",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onBackToHomeClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeporYellowPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Ir al inicio",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Text(text = value, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

private fun imprimirReciboHTML(context: Context, reservation: Reservation) {
    try {
        val webView = WebView(context)
        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: sans-serif; padding: 20px; text-align: center; }
                    .ticket { border: 2px solid #333; padding: 20px; border-radius: 12px; max-width: 400px; margin: auto; }
                    .title { font-size: 22px; font-weight: bold; color: #E0A800; }
                    .subtitle { font-size: 14px; color: #555; }
                    .row { display: flex; justify-content: space-between; margin: 8px 0; border-bottom: 1px dashed #ccc; padding-bottom: 4px; }
                    .total { font-size: 20px; font-weight: bold; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="ticket">
                    <div class="title">DeporPlaza</div>
                    <div class="subtitle">COMPROBANTE DIGITAL - ${reservation.receiptCode}</div>
                    <hr>
                    <div class="row"><span>Cancha:</span><span><b>${reservation.court.name}</b></span></div>
                    <div class="row"><span>Cliente:</span><span>${reservation.userName}</span></div>
                    <div class="row"><span>Fecha:</span><span>${reservation.date}</span></div>
                    <div class="row"><span>Horario:</span><span>${reservation.startTime} (${reservation.durationHours}h)</span></div>
                    <div class="row"><span>Pago:</span><span>${reservation.paymentMethod.displayName}</span></div>
                    <div class="row"><span>Estado:</span><span><b>${reservation.status.displayName}</b></span></div>
                    <div class="total">TOTAL: S/${reservation.totalPrice}</div>
                    <p>Reserva ${reservation.id} - Gracias por tu preferencia</p>
                </div>
            </body>
            </html>
        """.trimIndent()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = webView.createPrintDocumentAdapter("Comprobante_${reservation.receiptCode}")
                printManager.print("Comprobante_${reservation.receiptCode}", printAdapter, PrintAttributes.Builder().build())
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
