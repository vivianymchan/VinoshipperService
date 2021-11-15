package com.vinoshipper.exercise.api

import com.ninjasquad.springmockk.MockkBean
import com.vinoshipper.exercise.domain.calculator.AgeCalculator
import com.vinoshipper.exercise.domain.calculator.BusinessHourCalculator
import io.mockk.every
import io.mockk.slot
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest
class CalculatorControllerTests {
    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var businessHourCalculator: BusinessHourCalculator

    @MockkBean
    private lateinit var ageCalculator: AgeCalculator

    private val path = "/calculators"

    @Test
    fun `Business calculator happy path`() {
        val startSlot = slot<LocalDateTime>()
        val endSlot = slot<LocalDateTime>()
        every { businessHourCalculator.calculate(capture(startSlot), capture(endSlot)) } returns 14

        mvc.perform(
            MockMvcRequestBuilders.get("$path/business-hour")
                .param("startDateTime", "2021-11-14T08:30:25.000Z")
                .param("endDateTime", "2021-11-16T08:30:25.000-07:00")
        ).andExpect(MockMvcResultMatchers.content().json("{\"duration\":14,\"startUtc\":\"2021-11-14T08:00\",\"endUtc\":\"2021-11-16T15:00\"}"))

        assert(startSlot.captured.isEqual(LocalDateTime.of(2021, 11, 14, 8, 0)))
        assert(endSlot.captured.isEqual(LocalDateTime.of(2021, 11, 16, 15, 0)))
    }

    @Test
    fun `Age Validator happy path`() {
        every { ageCalculator.overDrinkingAge(any()) } returns true

        mvc.perform(
            MockMvcRequestBuilders.get("$path/of-age")
                .param("dateOfBirth", "2001-11-14")
        ).andExpect(MockMvcResultMatchers.content().json("{\"ofAgeToDrink\": true}"))
    }
}