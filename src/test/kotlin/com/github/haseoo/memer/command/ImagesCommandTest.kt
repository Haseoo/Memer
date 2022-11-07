package com.github.haseoo.memer.command

import com.github.haseoo.memer.TEST_ALLOWED_CHANNELS
import com.github.haseoo.memer.TEST_SERVER_ID
import com.github.haseoo.memer.TEST_SERVER_NAME
import com.github.haseoo.memer.service.ImageService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ImagesCommandTest {

    private lateinit var imageService: ImageService

    private lateinit var sut: ImagesCommand

    @BeforeEach
    fun setUp() {
        imageService = mockk()
        sut = ImagesCommand(TEST_SERVER_ID, TEST_SERVER_NAME, TEST_ALLOWED_CHANNELS, imageService)
    }

    @Test
    fun `images command should return private message with properly escaped url`() {
        //given
        val url = "https://example.com/images?id=xddd"
        every { imageService.generateImageLink(TEST_SERVER_ID, TEST_SERVER_NAME, TEST_ALLOWED_CHANNELS) } returns url
        //when
        val result = sut.execute()
        //then
        assertThat(result.isPublic).isFalse
        assertThat(result.replyMessage).contains("<$url>")
    }
}