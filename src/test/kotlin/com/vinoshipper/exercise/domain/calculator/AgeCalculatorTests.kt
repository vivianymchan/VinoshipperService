package com.vinoshipper.exercise.domain.calculator

import com.vinoshipper.exercise.domain.calculator.AgeCalculator.Companion.AGE_LIMIT
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class AgeCalculatorTests {
    private val ageCalculator = AgeCalculator()

    private val today = LocalDate.now()
    private val compareDay = if (today.month == Month.FEBRUARY && today.dayOfMonth == 29) {
        today.plusDays(1)
    } else {
        today
    }
    private val ofAgeDOB = compareDay.withYear(compareDay.year - AGE_LIMIT)

    @Test
    fun `A day shy`() {
        val oneDayShort = ofAgeDOB.plusDays(1)
        assert(!ageCalculator.overDrinkingAge(oneDayShort))
    }

    @Test
    fun `On birthday`() {
        assert(ageCalculator.overDrinkingAge(ofAgeDOB))
    }

    @Test
    fun `Of age drinking last month`() {
        val pastMonth = ofAgeDOB.minusMonths(1)
        assert(ageCalculator.overDrinkingAge(pastMonth))
    }

    @Test
    fun `Of age drinking next month`() {
        val nextMonth = ofAgeDOB.plusMonths(1)
        assert(!ageCalculator.overDrinkingAge(nextMonth))
    }

    @Test
    fun `Of age drinking last year`() {
        val lastYear = ofAgeDOB.minusYears(1)
        assert(ageCalculator.overDrinkingAge(lastYear))
    }

    @Test
    fun `Of age drink next year`() {
        val nextYear = ofAgeDOB.plusYears(1)
        assert(!ageCalculator.overDrinkingAge(nextYear))
    }

    @Test
    fun `Date in the future`() {
        assert(!ageCalculator.overDrinkingAge(today.plusDays(1)))
    }
}