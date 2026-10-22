package com.example.tucancha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.tucancha.model.UserRole
import com.example.tucancha.ui.MainViewModel
import com.example.tucancha.ui.components.BookingBottomSheet
import com.example.tucancha.ui.components.BottomNavBar
import com.example.tucancha.ui.components.PaymentBottomSheet
import com.example.tucancha.ui.components.ReceiptDialog
import com.example.tucancha.ui.screens.AdminDashboardScreen
import com.example.tucancha.ui.screens.FavoritesScreen
import com.example.tucancha.ui.screens.HomeScreen
import com.example.tucancha.ui.screens.LoginScreen
import com.example.tucancha.ui.screens.ProfileScreen
import com.example.tucancha.ui.theme.TuCanchaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TuCanchaTheme {
                TuCanchaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TuCanchaApp(viewModel: MainViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val courts by viewModel.courts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val favoriteCourtIds by viewModel.favoriteCourtIds.collectAsState()
    val reservations by viewModel.reservations.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val selectedCourtForBooking by viewModel.selectedCourtForBooking.collectAsState()
    val isPaymentStepOpen by viewModel.isPaymentStepOpen.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedTimeSlot by viewModel.selectedTimeSlot.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val confirmedReservation by viewModel.confirmedReservation.collectAsState()

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { name, email, role ->
                viewModel.registerUser(name, email, "123456", role)
            }
        )
    } else if (currentRole == UserRole.ADMINISTRADOR) {
        AdminDashboardScreen(
            reservations = reservations,
            onUpdateReservationStatus = { id, newStatus ->
                viewModel.updateReservationStatusAdmin(id, newStatus)
            },
            onLogoutClick = { viewModel.logout() }
        )
    } else {
        // User Mode
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    activeTab = activeTab,
                    onTabSelected = { viewModel.setActiveTab(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (activeTab) {
                    0 -> HomeScreen(
                        courts = courts,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.onCategorySelected(it) },
                        favoriteCourtIds = favoriteCourtIds,
                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                        onReserveClick = { viewModel.openBooking(it) },
                        userName = userProfile.name.split(" ").firstOrNull() ?: "Alejandro",
                        onLogoutClick = { viewModel.logout() }
                    )
                    1 -> FavoritesScreen(
                        courts = courts,
                        favoriteCourtIds = favoriteCourtIds,
                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                        onReserveClick = { viewModel.openBooking(it) }
                    )
                    2 -> ProfileScreen(
                        userProfile = userProfile,
                        reservations = reservations,
                        onCancelReservation = { viewModel.cancelReservation(it) },
                        onLogoutClick = { viewModel.logout() }
                    )
                }

                // Step 1: Booking Bottom Sheet (Time selection)
                selectedCourtForBooking?.let { court ->
                    if (!isPaymentStepOpen) {
                        BookingBottomSheet(
                            court = court,
                            selectedDay = selectedDay,
                            onDaySelected = { viewModel.selectDay(it) },
                            selectedTimeSlot = selectedTimeSlot,
                            onTimeSlotSelected = { viewModel.selectTimeSlot(it) },
                            selectedDuration = selectedDuration,
                            onDurationSelected = { viewModel.selectDuration(it) },
                            onContinueToPaymentClick = { viewModel.openPaymentStep() },
                            onDismissRequest = { viewModel.closeBooking() }
                        )
                    } else {
                        // Step 2: Payment Method Bottom Sheet
                        val timeSlot = selectedTimeSlot ?: "12:00"
                        val totalPrice = (court.pricePerHour * selectedDuration).toInt()
                        PaymentBottomSheet(
                            court = court,
                            selectedDay = selectedDay,
                            selectedTimeSlot = timeSlot,
                            selectedDuration = selectedDuration,
                            totalPrice = totalPrice,
                            onConfirmPayment = { method, opCode, phone ->
                                viewModel.confirmPaymentAndReservation(method, opCode, phone)
                            },
                            onBackToTimeSelection = { viewModel.closePaymentStep() },
                            onDismissRequest = { viewModel.closeBooking() }
                        )
                    }
                }

                // Digital Receipt Confirmation Dialog Modal
                confirmedReservation?.let { reservation ->
                    ReceiptDialog(
                        reservation = reservation,
                        onBackToHomeClick = {
                            viewModel.dismissConfirmation()
                            viewModel.setActiveTab(2) // Redirect to Reservas tab
                        }
                    )
                }
            }
        }
    }
}
