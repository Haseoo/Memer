package com.github.haseoo.memer.controller

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Profile("test")
class TestController(private val exceptionThrower: ExceptionThrower) {

    @Service
    class ExceptionThrower {
        fun throwException() {}
    }

    @GetMapping("/test/throw")
    fun execute() = exceptionThrower.throwException()
}