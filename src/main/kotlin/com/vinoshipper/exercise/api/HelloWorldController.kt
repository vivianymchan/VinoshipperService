package com.vinoshipper.exercise.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class HelloForm(
    val name:String
)

data class HelloResponse(
    val msg: String = "Hello!"
)

@RestController
class HelloWorldController {

    @GetMapping("/hello")
    fun hello(): HelloResponse {
        return HelloResponse()
    }

    @PostMapping("/hello")
    fun echo(@RequestBody form: HelloForm): HelloResponse {
        return HelloResponse("Hello ${form.name}!")
    }
}