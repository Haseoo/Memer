package com.github.haseoo.memer.controller

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.service.ImageService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URI


@Controller
class ImageController(private val _imageService: ImageService, private val _env: Env) {
    @GetMapping(path = ["/images"])
    fun init(@RequestParam(required = true) id: String): ResponseEntity<Void> = ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create("${_env.dscViewerRedirectUrl}/"))
        .header(
            HttpHeaders.SET_COOKIE, ResponseCookie.from("IMG_TOKEN", _imageService.generateImageToken(id))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1800L)
                .domain(_env.domain)
                .build().toString()
        )
        .build()

}