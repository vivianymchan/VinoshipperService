package com.vinoshipper.exercise.domain.calculator

data class BusinessHour(
    val workingDays: List<Int>,
    val offDays: List<Int>,
    val startHour: Int,
    val offHour: Int
)