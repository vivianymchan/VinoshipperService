package com.vinoshipper.exercise.domain.calculator

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AgeCalculator {
    fun overDrinkingAge(dateOfBirth: LocalDate): Boolean {
        val today = LocalDate.now()

        if (dateOfBirth > today) {
            return false
        }

        if (today.year - dateOfBirth.year > AGE_LIMIT) {
            return true
        }

        if (today.year - dateOfBirth.year == AGE_LIMIT && dateOfBirth.month < today.month) {
            return true
        }

        if (today.year - dateOfBirth.year == AGE_LIMIT && dateOfBirth.month == today.month && dateOfBirth.dayOfMonth <= today.dayOfMonth) {
            return true
        }

        return false
    }

    companion object {
        const val AGE_LIMIT = 21
    }

}