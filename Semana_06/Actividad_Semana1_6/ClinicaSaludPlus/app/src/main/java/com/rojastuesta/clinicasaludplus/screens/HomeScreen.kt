package com.rojastuesta.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojastuesta.clinicasaludplus.components.DoctorCard
import com.rojastuesta.clinicasaludplus.components.SpecialtyChip
import com.rojastuesta.clinicasaludplus.model.SampleData
import com.rojastuesta.clinicasaludplus.ui.theme.BackgroundLight
import com.rojastuesta.clinicasaludplus.ui.theme.PurpleLight
import com.rojastuesta.clinicasaludplus.ui.theme.PurplePrimary
import com.rojastuesta.clinicasaludplus.ui.theme.TextPrimary
import com.rojastuesta.clinicasaludplus.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onDoctorSelected: (String) -> Unit
) {
    var selectedSpecialty by remember { mutableStateOf("Todos") }

    val specialties = listOf("Todos", "Cardiología", "Pediatría", "Dermatología")

    val filteredDoctors = remember(selectedSpecialty) {
        if (selectedSpecialty == "Todos") {
            SampleData.doctors
        } else {
            SampleData.doctors.filter { it.specialty.contains(selectedSpecialty, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
        ) {
            Column {
                Text(
                    text = "Clínica Salud+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Hola, Juan",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
            }

            IconButton(onClick = onOpenDrawer) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PurpleLight)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = PurplePrimary
                    )
                }
            }
        }

        // Horizontal Specialty Filters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            items(specialties) { specialty ->
                SpecialtyChip(
                    specialtyName = specialty,
                    isSelected = selectedSpecialty == specialty,
                    onClick = { selectedSpecialty = specialty }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title: Médicos disponibles
        Text(
            text = "Médicos disponibles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Doctors List
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredDoctors) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = { onDoctorSelected(doctor.id) }
                )
            }
        }
    }
}
