package com.example.tucancha.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextSecondary

@Composable
fun TopHeader(
    userName: String = "Alejandro",
    avatarLetter: String = "A",
    estadoConexion: String = "Sincronizando...",
    esMySQLConectado: Boolean = false,
    onRetryConnection: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
                text = "DeporPlaza",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            // MySQL Connection Status Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (esMySQLConectado) Color(0xFF382A00) else Color(0xFF262B30))
                    .clickable { onRetryConnection() }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (esMySQLConectado) DeporYellowPrimary else Color(0xFFFF9800))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (esMySQLConectado) "MySQL Workbench Conectado" else "MySQL Desconectado (Toca para reintentar)",
                    color = if (esMySQLConectado) DeporYellowPrimary else Color(0xFFFFB74D),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(DeporYellowPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = avatarLetter,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
