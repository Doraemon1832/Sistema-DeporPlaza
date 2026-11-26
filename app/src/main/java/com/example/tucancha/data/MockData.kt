package com.example.tucancha.data

import com.example.tucancha.model.Court
import com.example.tucancha.model.DayOption
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.SportCategory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    fun obtenerDiasDinamicos(cantidadDias: Int = 14): List<DayOption> {
        val lista = mutableListOf<DayOption>()
        val calendar = Calendar.getInstance()
        val sdfFull = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDayNum = SimpleDateFormat("d", Locale.getDefault())

        val diasEspanol = arrayOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")

        for (i in 0 until cantidadDias) {
            val fullDate = sdfFull.format(calendar.time)
            val dayNum = sdfDayNum.format(calendar.time)
            val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val dayNameClean = diasEspanol[if (dayOfWeekIndex in 0..6) dayOfWeekIndex else 0]

            lista.add(DayOption(dayNameClean, dayNum, fullDate))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return lista
    }

    val sampleDays: List<DayOption>
        get() = obtenerDiasDinamicos(14)

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
