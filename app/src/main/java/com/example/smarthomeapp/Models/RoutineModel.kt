package com.example.smarthomeapp.Models

data class RoutineModel(
    val id: Int,
    val routineName: String,
    val time: String,
    val notification: String,
    val location: String,
    val lastRun: String
)