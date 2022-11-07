package com.github.haseoo.memer.controller

import com.github.haseoo.memer.config.ExceptionWebHandler
import com.github.haseoo.memer.domain.exception.LinkExpiredException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(classes = [TestController::class, ExceptionWebHandler::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class ExceptionMappingTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @MockkBean
    private lateinit var exceptionThrower: TestController.ExceptionThrower

    @Test
    fun `illegal state exception should map to 500 with exception message in error page`() {
        //given
        val message = "this is a message"
        every { exceptionThrower.throwException() } throws IllegalStateException(message)
        //when & then
        mockMvc.perform(get("/test/throw"))
            .andExpect(status().isInternalServerError)
            .andExpect(view().name("fragments/error.html"))
            .andExpect(model().attribute("message", message))
    }


    @Test
    fun `link expired exception should map to 400 with exception message in error page`() {
        //given
        every { exceptionThrower.throwException() } throws LinkExpiredException()
        //when & then
        mockMvc.perform(get("/test/throw"))
            .andExpect(status().isBadRequest)
            .andExpect(view().name("fragments/error.html"))
            .andExpect(model().attribute("message", "Link expired"))
    }
}