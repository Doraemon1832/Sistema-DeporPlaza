package com.example.tucancha.data

import com.example.tucancha.model.Court
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.SportCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ClienteApiMySQL {

    private var baseUrl: String = "http://10.0.2.2:8085/api"

    fun cambiarBaseUrl(nuevaUrl: String) {
        baseUrl = nuevaUrl
    }

    suspend fun verificarConexionServer(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/canchas")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 2000
            conn.readTimeout = 2000
            conn.requestMethod = "GET"
            val code = conn.responseCode
            conn.disconnect()
            code == 200
        } catch (t: Throwable) {
            false
        }
    }

    suspend fun registrarUsuarioApi(nombre: String, email: String, password: String, rol: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/usuarios")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val json = JSONObject().apply {
                put("nombre", nombre)
                put("email", email)
                put("password", password)
                put("rol", rol)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(json.toString())
            writer.flush()
            writer.close()

            val code = conn.responseCode
            conn.disconnect()
            code in 200..201
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    suspend fun obtenerCanchasApi(): List<Court> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Court>()
        try {
            val url = URL("$baseUrl/canchas")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "GET"

            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val respuesta = reader.readText()
                reader.close()

                val jsonArray = JSONArray(respuesta)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val sportStr = obj.optString("deporte", "PADEL")
                    val sportCategory = try {
                        SportCategory.valueOf(sportStr)
                    } catch (e: Exception) {
                        SportCategory.PADEL
                    }

                    val court = Court(
                        id = obj.getString("id"),
                        name = obj.getString("nombre"),
                        sport = sportCategory,
                        pricePerHour = obj.getInt("precio_por_hora"),
                        location = obj.getString("ubicacion"),
                        rating = obj.optDouble("calificacion", 4.7),
                        reviewsCount = obj.optInt("resenas_cantidad", 150),
                        capacityTag = obj.optString("capacidad", "2 vs 2"),
                        surfaceTag = obj.optString("superficie", "Césped monofilamento"),
                        lightingTag = obj.optString("iluminacion", "Iluminación LED"),
                        imageUrl = obj.getString("imagen_url"),
                        isAvailable = obj.optBoolean("disponible", true)
                    )
                    lista.add(court)
                }
            }
            conn.disconnect()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        lista
    }

    suspend fun guardarReservaApi(reserva: Reservation): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/reservas")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val json = JSONObject().apply {
                put("id", reserva.id)
                put("recibo_codigo", reserva.receiptCode)
                put("cancha_id", reserva.court.id)
                put("usuario_nombre", reserva.userName)
                put("fecha", reserva.date)
                put("dia_nombre", reserva.dayName)
                put("hora_inicio", reserva.startTime)
                put("duracion_horas", reserva.durationHours)
                put("precio_total", reserva.totalPrice)
                put("metodo_pago", reserva.paymentMethod.name)
                put("codigo_operacion", reserva.operationCode)
                put("celular", reserva.phoneNumber)
                put("estado", reserva.status.name)
                put("creado_en", reserva.createdAt)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(json.toString())
            writer.flush()
            writer.close()

            val code = conn.responseCode
            conn.disconnect()
            code in 200..201
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    suspend fun obtenerReservasApi(canchasMap: Map<String, Court>): List<Reservation> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Reservation>()
        try {
            val url = URL("$baseUrl/reservas")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "GET"

            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val respuesta = reader.readText()
                reader.close()

                val jsonArray = JSONArray(respuesta)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val canchaId = obj.getString("cancha_id")
                    val cancha = canchasMap[canchaId] ?: continue

                    val statusStr = obj.optString("estado", "CONFIRMADA")
                    val statusEnum = try {
                        ReservationStatus.valueOf(statusStr)
                    } catch (e: Exception) {
                        ReservationStatus.CONFIRMADA
                    }

                    val paymentStr = obj.optString("metodo_pago", "YAPE")
                    val paymentEnum = try {
                        PaymentMethod.valueOf(paymentStr)
                    } catch (e: Exception) {
                        PaymentMethod.YAPE
                    }

                    val reserva = Reservation(
                        id = obj.getString("id"),
                        receiptCode = obj.optString("recibo_codigo", "DP-0001055"),
                        court = cancha,
                        userName = obj.optString("usuario_nombre", "Alejandro Torres"),
                        date = obj.getString("fecha"),
                        dayName = obj.optString("dia_nombre", "Jue 22"),
                        startTime = obj.getString("hora_inicio"),
                        durationHours = obj.getDouble("duracion_horas"),
                        totalPrice = obj.getInt("precio_total"),
                        paymentMethod = paymentEnum,
                        operationCode = obj.optString("codigo_operacion", ""),
                        phoneNumber = obj.optString("celular", ""),
                        status = statusEnum,
                        createdAt = obj.optLong("creado_en", System.currentTimeMillis())
                    )
                    lista.add(reserva)
                }
            }
            conn.disconnect()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        lista
    }

    suspend fun cancelarReservaApi(reservaId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/reservas/$reservaId")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "DELETE"

            val code = conn.responseCode
            conn.disconnect()
            code == 200
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    suspend fun obtenerFavoritosApi(): Set<String> = withContext(Dispatchers.IO) {
        val conjunto = mutableSetOf<String>()
        try {
            val url = URL("$baseUrl/favoritos")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "GET"

            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val respuesta = reader.readText()
                reader.close()

                val jsonArray = JSONArray(respuesta)
                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getString(i)
                    conjunto.add(id)
                }
            }
            conn.disconnect()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        conjunto
    }

    suspend fun alternarFavoritoApi(canchaId: String, esFavorito: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/favoritos")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = if (esFavorito) "POST" else "DELETE"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val json = JSONObject().apply {
                put("cancha_id", canchaId)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(json.toString())
            writer.flush()
            writer.close()

            val code = conn.responseCode
            conn.disconnect()
            code in 200..204
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }
}
