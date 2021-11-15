package com.vinoshipper.exercise.domain.calculator

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BusinessHourCalculatorTests {
    private val businessHourCalculator = BusinessHourCalculator()
    private val friday = LocalDate.of(2021, 11, 12)
    private val saturday = LocalDate.of(2021, 11, 13)
    private val sunday = LocalDate.of(2021, 11, 14)
    private val monday = LocalDate.of(2021, 11, 15)
    private val tuesday = LocalDate.of(2021, 11, 16)
    private val nextTuesday = LocalDate.of(2021, 11, 23)

    @Test
    fun `Same Day - Work day 9am to 5pm`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(monday, LocalTime.of(9, 0)),
            LocalDateTime.of(monday, LocalTime.of(17, 0))
        ) == 8)
    }

    @Test
    fun `Same Day - Work day 7am to 4pm`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(monday, LocalTime.of(7, 0)),
            LocalDateTime.of(monday, LocalTime.of(16, 0))
        ) == 7)
    }

    @Test
    fun `Same Day - Work day 10am to 7pm`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(monday, LocalTime.of(10, 0)),
            LocalDateTime.of(monday, LocalTime.of(19, 0))
        ) == 7)
    }

    @Test
    fun `Same Day - Weekend 9am to 5pm`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(sunday, LocalTime.of(9, 0)),
            LocalDateTime.of(sunday, LocalTime.of(17, 0))
        ) == 0)
    }

    @Test
    fun `Weekends - Saturday 2pm to Sunday 6pm` () {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(saturday, LocalTime.of(14, 0)),
            LocalDateTime.of(sunday, LocalTime.of(18, 0))
        ) == 0)
    }

    @Test
    fun `Range - starts after hours on Friday`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(friday, LocalTime.of(23, 0)),
            LocalDateTime.of(monday, LocalTime.of(13, 0))
        ) == 4)
    }

    @Test
    fun `Range - ends before days starts`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(friday, LocalTime.of(23, 0)),
            LocalDateTime.of(monday, LocalTime.of(2, 0))
        ) == 0)
    }

    @Test
    fun `Range - last day is weekend`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(friday, LocalTime.of(6, 0)),
            LocalDateTime.of(sunday, LocalTime.of(23, 0))
        ) == 8)
    }

    @Test
    fun `Range - 2 consecutive working days`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(monday, LocalTime.of(10, 0)),
            LocalDateTime.of(tuesday, LocalTime.of(15, 0))
        ) == 13)
    }

    @Test
    fun `Range - 7 consecutive days`() {
        assert(businessHourCalculator.calculate(
            LocalDateTime.of(tuesday, LocalTime.of(10, 0)),
            LocalDateTime.of(nextTuesday, LocalTime.of(10, 0))
        ) == 40)
    }
}