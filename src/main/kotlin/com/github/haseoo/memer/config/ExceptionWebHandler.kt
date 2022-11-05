package com.github.haseoo.memer.config

import com.github.haseoo.memer.domain.exception.LinkExpiredException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class ExceptionWebHandler {

    @ExceptionHandler
    fun handle(exception: IllegalStateException): ModelAndView =
        ModelAndView(
            "fragments/error.html",
            toResponseModel(exception),
            HttpStatus.INTERNAL_SERVER_ERROR
        )

    @ExceptionHandler
    fun handle(exception: LinkExpiredException): ModelAndView =
        ModelAndView(
            "fragments/error.html",
            toResponseModel(exception),
            HttpStatus.BAD_REQUEST
        )

    private fun toResponseModel(exception: Exception) = mapOf("message" to exception.message)
}