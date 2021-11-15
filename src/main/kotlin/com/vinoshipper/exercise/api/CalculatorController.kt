package com.vinoshipper.exercise.api

import com.vinoshipper.exercise.domain.calculator.BusinessHourCalculator
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@RestController
@RequestMapping("calculators")
class CalculatorController(
    private val businessHourCalculator: BusinessHourCalculator
) {

    @GetMapping("/business-hour", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun calculateBusinessHours(@RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDateTime: OffsetDateTime,
                               @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDateTime: OffsetDateTime
    ): BusinessHourResponse {

        val (startDateHour, endDateHour) = normalizeInput(startDateTime, endDateTime)
        val duration = businessHourCalculator.calculate(startDateHour, endDateHour)

        return BusinessHourResponse(
            duration = duration,
            startUtc = startDateHour.toString(),
            endUtc = endDateHour.toString()
        )
    }

    private fun normalizeInput(startDateTime: OffsetDateTime, endDateTime: OffsetDateTime): Pair<LocalDateTime, LocalDateTime> {

        val startDateHour = truncateToHour(startDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
        val endDateHour = truncateToHour(endDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())

        if (startDateHour > endDateHour) {
            return Pair(endDateHour, startDateHour)
        }
        return Pair(startDateHour, endDateHour)

    }

    private fun truncateToHour(dateTime: LocalDateTime): LocalDateTime {
        return dateTime.withMinute(0).withSecond(0).withNano(0)
    }

}

data class BusinessHourResponse(
    val duration: Int,
    val startUtc: String,
    val endUtc: String
)