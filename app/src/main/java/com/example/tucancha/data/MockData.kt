package com.example.tucancha.data

import com.example.tucancha.model.Court
import com.example.tucancha.model.DayOption
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.SportCategory

object MockData {
    val sampleCourts = listOf(
        Court(
            id = "1",
            name = "Padel Club Los Pinos",
            sport = SportCategory.PADEL,
            pricePerHour = 110,
            location = "La Molina, Lima",
            rating = 4.7,
            reviewsCount = 156,
            capacityTag = "2 vs 2",
            surfaceTag = "Césped monofilamento",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?auto=format&fit=crop&w=800&q=80",
            isAvailable = true
        ),
        Court(
            id = "2",
            name = "Padel Blue Center",
            sport = SportCategory.PADEL,
            pricePerHour = 130,
            location = "San Borja, Lima",
            rating = 4.5,
            reviewsCount = 201,
            capacityTag = "2 vs 2",
            surfaceTag = "Pista panorámica",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0?auto=format&fit=crop&w=800&q=80",
            isAvailable = true
        ),
        Court(
            id = "3",
            name = "Estadio Norte",
            sport = SportCategory.FUTBOL,
            pricePerHour = 120,
            location = "San Isidro, Lima",
            rating = 4.8,
            reviewsCount = 234,
            capacityTag = "11 vs 11",
            surfaceTag = "Grass sintético",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=800&q=80",
            isAvailable = true
        ),
        Court(
            id = "4",
            name = "Campo Los Andes",
            sport = SportCategory.FUTBOL,
            pricePerHour = 80,
            location = "Miraflores, Lima",
            rating = 4.6,
            reviewsCount = 189,
            capacityTag = "5 vs 5",
            surfaceTag = "Grass sintético",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1529900748604-07564a03e7a6?auto=format&fit=crop&w=800&q=80",
            isAvailable = true
        ),
        Court(
            id = "5",
            name = "Arena Basket Pro",
            sport = SportCategory.BASQUET,
            pricePerHour = 95,
            location = "Surco, Lima",
            rating = 4.9,
            reviewsCount = 312,
            capacityTag = "5 vs 5",
            surfaceTag = "Parquet flotante",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1546519638-68e109498ffc?auto=format&fit=crop&w=800&q=80",
            isAvailable = true
        ),
        Court(
            id = "6",
            name = "Cancha Central Basket",
            sport = SportCategory.BASQUET,
            pricePerHour = 60,
            location = "Jesús María, Lima",
            rating = 4.3,
            reviewsCount = 98,
            capacityTag = "3 vs 3",
            surfaceTag = "Cemento pulido",
            lightingTag = "Iluminación LED",
            imageUrl = "https://images.unsplash.com/photo-1519861531473-9200262188bf?auto=format&fit=crop&w=800&q=80",
            isAvailable = false
        )
    )

    val sampleReservationsAdmin = listOf(
        Reservation(
            id = "BK-1058",
            receiptCode = "DP-0001058",
            court = sampleCourts[0],
            userName = "Alejandro Torres",
            date = "2026-10-22",
            dayName = "Jue 22",
            startTime = "12:00",
            durationHours = 1.0,
            totalPrice = 110,
            paymentMethod = PaymentMethod.AGENTE,
            operationCode = "456534",
            phoneNumber = "987654321",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1057",
            receiptCode = "DP-0001057",
            court = sampleCourts[0],
            userName = "Alejandro Torres",
            date = "2026-10-22",
            dayName = "Jue 22",
            startTime = "12:00",
            durationHours = 1.0,
            totalPrice = 110,
            paymentMethod = PaymentMethod.YAPE,
            operationCode = "654456",
            phoneNumber = "987654321",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1056",
            receiptCode = "DP-0001056",
            court = sampleCourts[0],
            userName = "Alejandro Torres",
            date = "2026-10-22",
            dayName = "Jue 22",
            startTime = "17:00",
            durationHours = 1.0,
            totalPrice = 110,
            paymentMethod = PaymentMethod.AGENTE,
            operationCode = "3425434",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1055",
            receiptCode = "DP-0001055",
            court = sampleCourts[0],
            userName = "Alejandro Torres",
            date = "2026-10-22",
            dayName = "Jue 22",
            startTime = "16:00",
            durationHours = 1.0,
            totalPrice = 110,
            paymentMethod = PaymentMethod.EFECTIVO,
            status = ReservationStatus.PENDIENTE
        ),
        Reservation(
            id = "BK-1054",
            receiptCode = "PS-0001054",
            court = sampleCourts[2],
            userName = "Alejandro Torres",
            date = "2026-10-22",
            dayName = "Jue 22",
            startTime = "20:00",
            durationHours = 1.0,
            totalPrice = 120,
            paymentMethod = PaymentMethod.YAPE,
            operationCode = "123456",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1048",
            receiptCode = "PS-0001048",
            court = sampleCourts[2],
            userName = "Diego Ramírez",
            date = "2026-06-18",
            dayName = "Mié 18",
            startTime = "19:00",
            durationHours = 2.0,
            totalPrice = 240,
            paymentMethod = PaymentMethod.YAPE,
            operationCode = "984721",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1049",
            receiptCode = "PS-0001049",
            court = sampleCourts[4],
            userName = "Lucía Mendoza",
            date = "2026-06-19",
            dayName = "Jue 19",
            startTime = "18:00",
            durationHours = 1.5,
            totalPrice = 142,
            paymentMethod = PaymentMethod.AGENTE,
            operationCode = "671093",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "BK-1050",
            receiptCode = "PS-0001050",
            court = sampleCourts[0],
            userName = "Martín Salazar",
            date = "2026-06-20",
            dayName = "Vie 20",
            startTime = "10:00",
            durationHours = 1.0,
            totalPrice = 110,
            paymentMethod = PaymentMethod.YAPE,
            operationCode = "298413",
            status = ReservationStatus.CONFIRMADA
        )
    )

    val sampleDays = listOf(
        DayOption("Mié", "21", "2026-10-21"),
        DayOption("Jue", "22", "2026-10-22"),
        DayOption("Vie", "23", "2026-10-23"),
        DayOption("Sáb", "24", "2026-10-24"),
        DayOption("Dom", "25", "2026-10-25"),
        DayOption("Lun", "26", "2026-10-26")
    )

    val sampleTimeSlots = listOf(
        "07:00", "08:00", "09:00", "10:00",
        "11:00", "12:00", "13:00", "14:00",
        "15:00", "16:00", "17:00", "18:00",
        "19:00", "20:00", "21:00", "22:00"
    )

    val sampleDurations = listOf(
        1.0 to "1 h",
        1.5 to "1.5 h",
        2.0 to "2 h",
        3.0 to "3 h"
    )
}
