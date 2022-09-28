package com.github.haseoo.memer.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class MainPageController {
    @GetMapping("/")
    fun home() = ModelAndView("fragments/home.html")
}