package com.example.tucancha.model

enum class SportCategory(val displayName: String, val emoji: String) {
    TODOS("Todos", "⚽"),
    FUTBOL("Fútbol", "⚽"),
    BASQUET("Básquet", "🏀"),
    PADEL("Pádel", "🎾")
}

enum class UserRole(val displayName: String) {
    USUARIO("Usuario"),
    ADMINISTRADOR("Administrador")
}

enum class PaymentMethod(val id: String, val displayName: String, val shortCode: String) {
    YAPE("YAPE", "Yape", "Y"),
    AGENTE("AGENTE", "Agente bancario", "AB"),
    EFECTIVO("EFECTIVO", "Efectivo en caja", "S/")
}

enum class ReservationStatus(val displayName: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada")
}

data class Court(
    val id: String,
    val name: String,
    val sport: SportCategory,
    val pricePerHour: Int,
    val location: String,
    val rating: Double,
    val reviewsCount: Int,
    val capacityTag: String,
    val surfaceTag: String,
    val lightingTag: String,
    val imageUrl: String,
    val isAvailable: Boolean = true,
    val description: String = "Cancha profesional equipada con la mejor iluminación y servicios para tu partido."
)

data class Reservation(
    val id: String,
    val receiptCode: String = "DP-0001055",
    val court: Court,
    val userName: String = "Alejandro Torres",
    val date: String,          // e.g. "2026-10-22"
    val dayName: String,       // e.g. "Mié 21"
    val startTime: String,     // e.g. "16:00"
    val durationHours: Double, // e.g. 1.0, 1.5, 2.0, 3.0
    val totalPrice: Int,
    val paymentMethod: PaymentMethod = PaymentMethod.YAPE,
    val operationCode: String = "",
    val phoneNumber: String = "",
    val status: ReservationStatus = ReservationStatus.CONFIRMADA,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserProfile(
    val id: String = "1",
    val name: String = "Alejandro Torres",
    val email: String = "alejandro@email.com",
    val role: UserRole = UserRole.USUARIO,
    val badge: String = "Miembro Premium",
    val avatarLetter: String = "A"
)

data class DayOption(
    val dayOfWeek: String, // e.g. "Mié"
    val dayNumber: String, // e.g. "21"
    val fullDate: String   // e.g. "2026-10-21"
)
