package com.github.haseoo.memer.controller

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.service.ImageService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [ImageController::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class ImagesControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var imageService: ImageService

    @MockkBean
    private lateinit var env: Env

    @Test
    fun `meme list page contains server name retrieved form Discord client`() {
        //given
        val key = "thisisakey"
        val domain = "example.com"
        val token = "token"
        val cookieName = "IMG_TOKEN"
        val dscViewerUrl = "example.view.com"
        every { imageService.generateImageToken(key) } returns token
        every { env.domain } returns domain
        every { env.dscViewerRedirectUrl } returns dscViewerUrl
        //when & then
        mockMvc.perform(get("/images").queryParam("id", key))
            .andExpect(status().is3xxRedirection)
            .andExpect(cookie().domain(cookieName, domain))
            .andExpect(cookie().value(cookieName, token))
            .andExpect(cookie().path(cookieName, "/"))
            .andExpect(cookie().secure(cookieName, true))
            .andExpect(cookie().httpOnly(cookieName, true))
            .andExpect(cookie().maxAge(cookieName, TimeUnit.MINUTES.toSeconds(30).toInt()))
    }
}