package com.example.tucancha.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tucancha.data.ConexionMySQL
import com.example.tucancha.data.MockData
import com.example.tucancha.model.Court
import com.example.tucancha.model.DayOption
import com.example.tucancha.model.PaymentMethod
import com.example.tucancha.model.Reservation
import com.example.tucancha.model.ReservationStatus
import com.example.tucancha.model.SportCategory
import com.example.tucancha.model.UserProfile
import com.example.tucancha.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.USUARIO)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _courts = MutableStateFlow<List<Court>>(MockData.sampleCourts)
    val courts: StateFlow<List<Court>> = _courts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SportCategory.TODOS)
    val selectedCategory: StateFlow<SportCategory> = _selectedCategory.asStateFlow()

    private val _favoriteCourtIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteCourtIds: StateFlow<Set<String>> = _favoriteCourtIds.asStateFlow()

    private val _reservations = MutableStateFlow<List<Reservation>>(MockData.sampleReservationsAdmin)
    val reservations: StateFlow<List<Reservation>> = _reservations.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _estadoConexion = MutableStateFlow("Comprobando conexión a MySQL Workbench...")
    val estadoConexion: StateFlow<String> = _estadoConexion.asStateFlow()

    private val _esMySQLConectado = MutableStateFlow(false)
    val esMySQLConectado: StateFlow<Boolean> = _esMySQLConectado.asStateFlow()

    // Booking state
    private val _selectedCourtForBooking = MutableStateFlow<Court?>(null)
    val selectedCourtForBooking: StateFlow<Court?> = _selectedCourtForBooking.asStateFlow()

    private val _isPaymentStepOpen = MutableStateFlow(false)
    val isPaymentStepOpen: StateFlow<Boolean> = _isPaymentStepOpen.asStateFlow()

    private val _selectedDay = MutableStateFlow(MockData.sampleDays.first())
    val selectedDay: StateFlow<DayOption> = _selectedDay.asStateFlow()

    private val _selectedTimeSlot = MutableStateFlow<String?>(null)
    val selectedTimeSlot: StateFlow<String?> = _selectedTimeSlot.asStateFlow()

    private val _selectedDuration = MutableStateFlow(1.0)
    val selectedDuration: StateFlow<Double> = _selectedDuration.asStateFlow()

    private val _confirmedReservation = MutableStateFlow<Reservation?>(null)
    val confirmedReservation: StateFlow<Reservation?> = _confirmedReservation.asStateFlow()

    private val _activeTab = MutableStateFlow(0) // 0 = Inicio, 1 = Favoritos, 2 = Perfil
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    init {
        cargarDatosDesdeMySQL()
    }

    fun login(name: String, email: String, role: UserRole) {
        _currentRole.value = role
        _isLoggedIn.value = true
        _userProfile.value = UserProfile(
            name = name,
            email = email,
            role = role,
            badge = if (role == UserRole.ADMINISTRADOR) "Super Admin" else "Miembro Premium",
            avatarLetter = name.firstOrNull()?.uppercase() ?: "A"
        )
    }

    fun registerUser(name: String, email: String, password: String, role: UserRole) {
        login(name, email, role)
        viewModelScope.launch {
            val exito = ConexionMySQL.registrarUsuarioBD(name, email, password, role.name)
            if (exito) {
                _esMySQLConectado.value = true
                _estadoConexion.value = "Conectado a MySQL Workbench (tucancha_db)"
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun cargarDatosDesdeMySQL() {
        viewModelScope.launch {
            val estaConectado = ConexionMySQL.verificarConexion()
            _esMySQLConectado.value = estaConectado

            if (estaConectado) {
                _estadoConexion.value = "Conectado a MySQL Workbench (tucancha_db)"

                val canchasBD = ConexionMySQL.obtenerCanchasBD()
                if (canchasBD.isNotEmpty()) {
                    _courts.value = canchasBD
                }

                val mapaCanchas = _courts.value.associateBy { it.id }

                val reservasBD = ConexionMySQL.obtenerReservasBD(mapaCanchas)
                if (reservasBD.isNotEmpty()) {
                    _reservations.value = reservasBD
                }

                val favoritosBD = ConexionMySQL.obtenerFavoritosBD()
                _favoriteCourtIds.value = favoritosBD
            } else {
                _estadoConexion.value = "Sin conexión a MySQL Workbench. Usando datos de prueba."
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: SportCategory) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(courtId: String) {
        val estaEnFavoritos = _favoriteCourtIds.value.contains(courtId)
        val nuevoEstado = !estaEnFavoritos

        _favoriteCourtIds.value = if (estaEnFavoritos) {
            _favoriteCourtIds.value - courtId
        } else {
            _favoriteCourtIds.value + courtId
        }

        viewModelScope.launch {
            val exito = ConexionMySQL.alternarFavoritoBD(courtId, nuevoEstado)
            if (exito) {
                _esMySQLConectado.value = true
                _estadoConexion.value = "Conectado a MySQL Workbench (tucancha_db)"
            }
        }
    }

    fun openBooking(court: Court) {
        _selectedCourtForBooking.value = court
        _isPaymentStepOpen.value = false
        _selectedDay.value = MockData.sampleDays.first()
        _selectedTimeSlot.value = null
        _selectedDuration.value = 1.0
    }

    fun openPaymentStep() {
        _isPaymentStepOpen.value = true
    }

    fun closePaymentStep() {
        _isPaymentStepOpen.value = false
    }

    fun closeBooking() {
        _selectedCourtForBooking.value = null
        _isPaymentStepOpen.value = false
    }

    fun selectDay(day: DayOption) {
        _selectedDay.value = day
    }

    fun selectTimeSlot(time: String) {
        _selectedTimeSlot.value = time
    }

    fun selectDuration(duration: Double) {
        _selectedDuration.value = duration
    }

    fun confirmPaymentAndReservation(
        paymentMethod: PaymentMethod,
        operationCode: String,
        phoneNumber: String
    ) {
        val court = _selectedCourtForBooking.value ?: return
        val day = _selectedDay.value
        val time = _selectedTimeSlot.value ?: return
        val duration = _selectedDuration.value
        val totalPrice = (court.pricePerHour * duration).toInt()

        val initialStatus = if (paymentMethod == PaymentMethod.EFECTIVO) {
            ReservationStatus.PENDIENTE
        } else {
            ReservationStatus.CONFIRMADA
        }

        val codeNumber = (1000..9999).random()
        val newReservation = Reservation(
            id = "BK-$codeNumber",
            receiptCode = "DP-000$codeNumber",
            court = court,
            userName = _userProfile.value.name,
            date = day.fullDate,
            dayName = "${day.dayOfWeek} ${day.dayNumber}",
            startTime = time,
            durationHours = duration,
            totalPrice = totalPrice,
            paymentMethod = paymentMethod,
            operationCode = operationCode,
            phoneNumber = phoneNumber,
            status = initialStatus
        )

        _reservations.value = listOf(newReservation) + _reservations.value
        _confirmedReservation.value = newReservation
        _selectedCourtForBooking.value = null
        _isPaymentStepOpen.value = false

        // Intentar guardar en MySQL de inmediato
        viewModelScope.launch {
            val exito = ConexionMySQL.guardarReservaBD(newReservation)
            if (exito) {
                _esMySQLConectado.value = true
                _estadoConexion.value = "Conectado a MySQL Workbench (tucancha_db)"
            }
        }
    }

    fun dismissConfirmation() {
        _confirmedReservation.value = null
    }

    fun cancelReservation(reservationId: String) {
        _reservations.value = _reservations.value.filter { it.id != reservationId }

        viewModelScope.launch {
            ConexionMySQL.cancelarReservaBD(reservationId)
        }
    }

    fun updateReservationStatusAdmin(reservationId: String, newStatus: ReservationStatus) {
        _reservations.value = _reservations.value.map { reservation ->
            if (reservation.id == reservationId) {
                reservation.copy(status = newStatus)
            } else {
                reservation
            }
        }

        viewModelScope.launch {
            val res = _reservations.value.find { it.id == reservationId }
            if (res != null) {
                ConexionMySQL.guardarReservaBD(res)
            }
        }
    }

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }
}
