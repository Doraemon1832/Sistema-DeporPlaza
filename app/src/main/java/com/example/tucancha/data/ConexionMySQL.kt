package com.example.tucancha.data

import com.example.tucancha.model.Court
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.model.SportCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object ConexionMySQL {

    private var host: String = "10.0.2.2"
    private var puerto: String = "3306"
    private var baseDatos: String = "tucancha_db"
    private var usuarioBD: String = "root"
    private var claveBD: String = "root"

    fun cambiarConfiguracion(nuevoHost: String, nuevoPuerto: String = "3306", nuevoUsuario: String = "root", nuevaClave: String = "root") {
        host = nuevoHost
        puerto = nuevoPuerto
        usuarioBD = nuevoUsuario
        claveBD = nuevaClave
    }

    private fun obtenerConexionJDBC(): Connection? {
        return try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://$host:$puerto/$baseDatos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=2000"
            DriverManager.getConnection(url, usuarioBD, claveBD)
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun verificarConexion(): Boolean = withContext(Dispatchers.IO) {
        val apiOk = ClienteApiMySQL.verificarConexionServer()
        if (apiOk) {
            return@withContext true
        }

        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC()
            val ok = conn != null && !conn.isClosed
            conn?.close()
            ok
        } catch (t: Throwable) {
            false
        }
    }

    suspend fun registrarUsuarioBD(nombre: String, email: String, password: String, rol: String): Boolean = withContext(Dispatchers.IO) {
        val exitoApi = ClienteApiMySQL.registrarUsuarioApi(nombre, email, password, rol)
        if (exitoApi) return@withContext true

        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext false
            val sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), password = VALUES(password)"
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, nombre)
            stmt.setString(2, email)
            stmt.setString(3, password)
            stmt.setString(4, rol)
            val filas = stmt.executeUpdate()
            stmt.close()
            filas > 0
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
    }

    suspend fun obtenerCanchasBD(): List<Court> = withContext(Dispatchers.IO) {
        val listaApi = ClienteApiMySQL.obtenerCanchasApi()
        if (listaApi.isNotEmpty()) return@withContext listaApi

        val lista = mutableListOf<Court>()
        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext emptyList()
            val query = "SELECT * FROM canchas"
            val stmt = conn.prepareStatement(query)
            val rs: ResultSet = stmt.executeQuery()

            while (rs.next()) {
                val sportString = rs.getString("deporte") ?: "PADEL"
                val sportCategory = try {
                    SportCategory.valueOf(sportString)
                } catch (t: Throwable) {
                    SportCategory.PADEL
                }

                val court = Court(
                    id = rs.getString("id"),
                    name = rs.getString("nombre"),
                    sport = sportCategory,
                    pricePerHour = rs.getInt("precio_por_hora"),
                    location = rs.getString("ubicacion"),
                    rating = rs.getDouble("calificacion"),
                    reviewsCount = rs.getInt("resenas_cantidad"),
                    capacityTag = rs.getString("capacidad"),
                    surfaceTag = rs.getString("superficie"),
                    lightingTag = rs.getString("iluminacion"),
                    imageUrl = rs.getString("imagen_url"),
                    isAvailable = rs.getBoolean("disponible")
                )
                lista.add(court)
            }
            rs.close()
            stmt.close()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
        lista
    }

    suspend fun guardarReservaBD(reserva: Reservation): Boolean = withContext(Dispatchers.IO) {
        val exitoApi = ClienteApiMySQL.guardarReservaApi(reserva)
        if (exitoApi) return@withContext true

        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext false
            val sql = "INSERT INTO reservas (id, recibo_codigo, cancha_id, usuario_nombre, fecha, dia_nombre, hora_inicio, duracion_horas, precio_total, metodo_pago, codigo_operacion, celular, estado, creado_en) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE estado = VALUES(estado)"
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, reserva.id)
            stmt.setString(2, reserva.receiptCode)
            stmt.setString(3, reserva.court.id)
            stmt.setString(4, reserva.userName)
            stmt.setString(5, reserva.date)
            stmt.setString(6, reserva.dayName)
            stmt.setString(7, reserva.startTime)
            stmt.setDouble(8, reserva.durationHours)
            stmt.setInt(9, reserva.totalPrice)
            stmt.setString(10, reserva.paymentMethod.name)
            stmt.setString(11, reserva.operationCode)
            stmt.setString(12, reserva.phoneNumber)
            stmt.setString(13, reserva.status.name)
            stmt.setLong(14, reserva.createdAt)

            val filas = stmt.executeUpdate()
            stmt.close()
            filas > 0
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
    }

    suspend fun obtenerReservasBD(canchasMap: Map<String, Court>): List<Reservation> = withContext(Dispatchers.IO) {
        val reservasApi = ClienteApiMySQL.obtenerReservasApi(canchasMap)
        if (reservasApi.isNotEmpty()) return@withContext reservasApi

        val lista = mutableListOf<Reservation>()
        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext emptyList()
            val sql = "SELECT * FROM reservas ORDER BY creado_en DESC"
            val stmt = conn.prepareStatement(sql)
            val rs = stmt.executeQuery()

            while (rs.next()) {
                val canchaId = rs.getString("cancha_id")
                val cancha = canchasMap[canchaId] ?: continue

                val statusStr = rs.getString("estado") ?: "CONFIRMADA"
                val statusEnum = try {
                    ReservationStatus.valueOf(statusStr)
                } catch (t: Throwable) {
                    ReservationStatus.CONFIRMADA
                }

                val paymentStr = rs.getString("metodo_pago") ?: "YAPE"
                val paymentEnum = try {
                    PaymentMethod.valueOf(paymentStr)
                } catch (t: Throwable) {
                    PaymentMethod.YAPE
                }

                val reserva = Reservation(
                    id = rs.getString("id"),
                    receiptCode = rs.getString("recibo_codigo") ?: "DP-0001055",
                    court = cancha,
                    userName = rs.getString("usuario_nombre") ?: "Alejandro Torres",
                    date = rs.getString("fecha"),
                    dayName = rs.getString("dia_nombre"),
                    startTime = rs.getString("hora_inicio"),
                    durationHours = rs.getDouble("duracion_horas"),
                    totalPrice = rs.getInt("precio_total"),
                    paymentMethod = paymentEnum,
                    operationCode = rs.getString("codigo_operacion") ?: "",
                    phoneNumber = rs.getString("celular") ?: "",
                    status = statusEnum,
                    createdAt = rs.getLong("creado_en")
                )
                lista.add(reserva)
            }
            rs.close()
            stmt.close()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
        lista
    }

    suspend fun cancelarReservaBD(reservaId: String): Boolean = withContext(Dispatchers.IO) {
        val exitoApi = ClienteApiMySQL.cancelarReservaApi(reservaId)
        if (exitoApi) return@withContext true

        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext false
            val sql = "DELETE FROM reservas WHERE id = ?"
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, reservaId)
            val filas = stmt.executeUpdate()
            stmt.close()
            filas > 0
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
    }

    suspend fun obtenerFavoritosBD(): Set<String> = withContext(Dispatchers.IO) {
        val favsApi = ClienteApiMySQL.obtenerFavoritosApi()
        if (favsApi.isNotEmpty()) return@withContext favsApi

        val conjunto = mutableSetOf<String>()
        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext emptySet()
            val sql = "SELECT cancha_id FROM favoritos"
            val stmt = conn.prepareStatement(sql)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                conjunto.add(rs.getString("cancha_id"))
            }
            rs.close()
            stmt.close()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
        conjunto
    }

    suspend fun alternarFavoritoBD(canchaId: String, esFavorito: Boolean): Boolean = withContext(Dispatchers.IO) {
        val exitoApi = ClienteApiMySQL.alternarFavoritoApi(canchaId, esFavorito)
        if (exitoApi) return@withContext true

        var conn: Connection? = null
        try {
            conn = obtenerConexionJDBC() ?: return@withContext false
            if (esFavorito) {
                val sqlDelete = "DELETE FROM favoritos WHERE cancha_id = ?"
                val stmtDelete = conn.prepareStatement(sqlDelete)
                stmtDelete.setString(1, canchaId)
                stmtDelete.executeUpdate()
                stmtDelete.close()

                val sqlInsert = "INSERT INTO favoritos (cancha_id) VALUES (?)"
                val stmtInsert = conn.prepareStatement(sqlInsert)
                stmtInsert.setString(1, canchaId)
                stmtInsert.executeUpdate()
                stmtInsert.close()
            } else {
                val sql = "DELETE FROM favoritos WHERE cancha_id = ?"
                val stmt = conn.prepareStatement(sql)
                stmt.setString(1, canchaId)
                stmt.executeUpdate()
                stmt.close()
            }
            true
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        } finally {
            try { conn?.close() } catch (t: Throwable) {}
        }
    }
}
