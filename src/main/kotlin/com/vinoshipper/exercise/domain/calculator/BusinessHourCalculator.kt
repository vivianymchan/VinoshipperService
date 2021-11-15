package com.vinoshipper.exercise.domain.calculator

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class BusinessHourCalculator {
    fun calculate(startDateHour: LocalDateTime, endDateHour: LocalDateTime): Int {

        if (startDateHour == endDateHour) return 0

        val firstWorkDateHour = findFirstWorkingDay(startDateHour, endDateHour) ?: return 0
        val lastWorkDateHour = findLastWorkingDay(firstWorkDateHour, endDateHour) ?: return 0

        if (isSameDay(firstWorkDateHour.toLocalDate(), lastWorkDateHour.toLocalDate())) {
            return lastWorkDateHour.hour - firstWorkDateHour.hour
        }

        val hourOfFirstDay = businessHour.offHour - firstWorkDateHour.hour
        val hourOfLastDay = lastWorkDateHour.hour - businessHour.startHour

        val workingDatesInBetween = countWorkingDays(
            firstWorkDateHour.toLocalDate().plusDays(1),
            lastWorkDateHour.toLocalDate().minusDays(1)
        )
        val workingHoursPerDay = businessHour.offHour - businessHour.startHour

        return hourOfFirstDay + hourOfLastDay + workingDatesInBetween * workingHoursPerDay

    }

    private fun findFirstWorkingDay(startDateTime: LocalDateTime, endDateTime: LocalDateTime): LocalDateTime? {

        var start = startDateTime.toLocalDate()
        while (!isWorkingDay(start)) {
            start = start.plusDays(1)
        }

        if (start > endDateTime.toLocalDate()) {
            return null
        }

        if (!isSameDay(start, startDateTime.toLocalDate())) {
            return LocalDateTime.of(start, LocalTime.of(businessHour.startHour, 0))
        }

        if (withinBusinessHours(startDateTime.hour)) {
            return LocalDateTime.of(start, LocalTime.of(startDateTime.hour, 0))
        }

        if (startDateTime.hour < businessHour.startHour) {
            return LocalDateTime.of(start, LocalTime.of(businessHour.startHour, 0))
        }

        return findFirstWorkingDay(
            LocalDateTime.of(start.plusDays(1), LocalTime.of(businessHour.startHour, 0)), endDateTime
        )
    }

    private fun findLastWorkingDay(startDateTime: LocalDateTime, endDateTime: LocalDateTime): LocalDateTime? {
        var end = endDateTime.toLocalDate()

        while(!isWorkingDay(end)) {
            end = end.minusDays(1)
        }

        if (endDateTime < startDateTime) {
            return null
        }

        if (!isSameDay(end, endDateTime.toLocalDate())) {
            return LocalDateTime.of(end, LocalTime.of(businessHour.offHour, 0))
        }

        if (withinBusinessHours(endDateTime.hour)) {
            return LocalDateTime.of(end, LocalTime.of(endDateTime.hour, 0))
        }

        if (endDateTime.hour > businessHour.offHour) {
            return LocalDateTime.of(end, LocalTime.of(businessHour.offHour, 0))
        }

        return findLastWorkingDay(
            startDateTime,
            LocalDateTime.of(end.minusDays(1), LocalTime.of(businessHour.offHour, 0))
        )
    }

    private fun countWorkingDays(startDate: LocalDate, endDate: LocalDate): Int {
        var days = 0
        var start = startDate

        while(start <= endDate) {
            if (isWorkingDay(start)) {
                days += 1
            }

            start = start.plusDays(1)
        }

        return days
    }

    private fun isWorkingDay(date: LocalDate): Boolean {
        return businessHour.workingDays.contains(date.dayOfWeek.value)
    }

    private fun withinBusinessHours(hour: Int): Boolean {
        return hour >= businessHour.startHour && hour <= businessHour.offHour
    }

    private fun isSameDay(date1: LocalDate, date2: LocalDate): Boolean {
        return date1.isEqual(date2)
    }

    companion object {
        val businessHour = BusinessHour(
            workingDays = listOf(1, 2, 3, 4, 5),
            offDays = listOf(6, 7),
            startHour = 9,
            offHour = 17
        )
    }
}