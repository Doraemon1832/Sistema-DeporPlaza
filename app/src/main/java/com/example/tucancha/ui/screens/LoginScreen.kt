package com.example.tucancha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tucancha.model.UserRole
import com.example.tucancha.ui.theme.DarkBackground
import com.example.tucancha.ui.theme.DarkCardBorder
import com.example.tucancha.ui.theme.DarkSurface
import com.example.tucancha.ui.theme.DeporYellowPrimary
import com.example.tucancha.ui.theme.TextPrimary
import com.example.tucancha.ui.theme.TextSecondary
import com.example.tucancha.ui.theme.UnavailableRed

@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, email: String, role: UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisteringMode by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.USUARIO) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("alejandro@email.com") }
    var password by remember { mutableStateOf("123456") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // DeporPlaza Yellow Grid Icon Box
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DeporYellowPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = "DeporPlaza Logo",
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RESERVA. JUEGA. REPITE.",
            color = DeporYellowPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "DeporPlaza",
            color = TextPrimary,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tu próxima cancha está a unos pasos.",
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main Login/Register Card Box
        val cardShape = RoundedCornerShape(24.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, cardShape)
                .padding(20.dp)
        ) {
            // Role Tab Selector Box
            val tabShape = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(tabShape)
                    .background(Color(0xFF121518))
                    .padding(4.dp)
            ) {
                // Usuario Tab
                val isUsuario = selectedRole == UserRole.USUARIO
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isUsuario) DeporYellowPrimary else Color.Transparent)
                        .clickable {
                            selectedRole = UserRole.USUARIO
                            email = "alejandro@email.com"
                            errorMessage = null
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isUsuario) Color.Black else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Usuario",
                            color = if (isUsuario) Color.Black else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // Administrador Tab
                val isAdmin = selectedRole == UserRole.ADMINISTRADOR
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isAdmin) DeporYellowPrimary else Color.Transparent)
                        .clickable {
                            selectedRole = UserRole.ADMINISTRADOR
                            email = "admin@deporplaza.com"
                            isRegisteringMode = false
                            errorMessage = null
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = if (isAdmin) Color.Black else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Administrador",
                            color = if (isAdmin) Color.Black else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title & Subtitle based on mode
            if (isRegisteringMode) {
                Text(
                    text = "Crear nueva cuenta",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Regístrate para reservar tus canchas favoritas.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            } else if (selectedRole == UserRole.USUARIO) {
                Text(
                    text = "Bienvenido de vuelta",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ingresa para gestionar tus partidos.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            } else {
                Text(
                    text = "Acceso administrativo",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Monitorea reservas y operaciones.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Error Message
            errorMessage?.let { error ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF3A1E1E))
                        .padding(10.dp)
                ) {
                    Text(
                        text = error,
                        color = UnavailableRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Full Name Field (Only in Register mode)
            if (isRegisteringMode) {
                Text(
                    text = "Nombre completo",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        errorMessage = null
                    },
                    placeholder = { Text(text = "Ej. Carlos Mendoza", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeporYellowPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = Color(0xFF121518),
                        unfocusedContainerColor = Color(0xFF121518),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Correo Field
            Text(
                text = "Correo",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeporYellowPrimary,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedContainerColor = Color(0xFF121518),
                    unfocusedContainerColor = Color(0xFF121518),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Contraseña Field
            Text(
                text = "Contraseña",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeporYellowPrimary,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedContainerColor = Color(0xFF121518),
                    unfocusedContainerColor = Color(0xFF121518),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // Confirm Password Field (Only in Register mode)
            if (isRegisteringMode) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Confirmar contraseña",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Button(
                onClick = {
                    if (isRegisteringMode) {
                        if (fullName.isBlank()) {
                            errorMessage = "Por favor ingresa tu nombre completo"
                            return@Button
                        }
                        if (email.isBlank() || !email.contains("@")) {
                            errorMessage = "Por favor ingresa un correo válido"
                            return@Button
                        }
                        if (password.length < 4) {
                            errorMessage = "La contraseña debe tener al menos 4 caracteres"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Las contraseñas no coinciden"
                            return@Button
                        }
                        onLoginSuccess(fullName, email, selectedRole)
                    } else {
                        if (email.isBlank()) {
                            errorMessage = "Por favor ingresa tu correo"
                            return@Button
                        }
                        val derivedName = if (selectedRole == UserRole.ADMINISTRADOR) {
                            "Administrador DeporPlaza"
                        } else {
                            if (email.contains("alejandro")) "Alejandro Torres" else email.substringBefore("@")
                        }
                        onLoginSuccess(derivedName, email, selectedRole)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeporYellowPrimary,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isRegisteringMode) "Crear cuenta en DeporPlaza" else "Ingresar a DeporPlaza",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Bottom Toggle Link (Usuario mode only)
            if (selectedRole == UserRole.USUARIO) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            isRegisteringMode = !isRegisteringMode
                            errorMessage = null
                        }
                    ) {
                        Text(
                            text = if (isRegisteringMode) "¿Ya tienes una cuenta? Inicia sesión" else "¿No tienes una cuenta? Regístrate aquí",
                            color = DeporYellowPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
