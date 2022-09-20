package com.github.haseoo.memer.command

import com.github.haseoo.memer.COMMAND_ARGS
import com.github.haseoo.memer.TEST_MEME_NAME
import com.github.haseoo.memer.TEST_MEME_URL
import com.github.haseoo.memer.TEST_SERVER_ID
import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.domain.exception.ArgumentNotPresentException
import com.github.haseoo.memer.repository.MemeRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetMemeCommandTest {

    private val memerUrl = "http://test.example.com"

    private lateinit var memeRepository: MemeRepository

    private lateinit var env: Env

    private lateinit var sut: GetMemeCommand

    @BeforeEach
    fun setUp() {
        memeRepository = mockk()
        env = mockk()
        every { env.memerUrl } returns memerUrl
        sut = GetMemeCommand(memeRepository, TEST_SERVER_ID, COMMAND_ARGS, env)
    }

    @Test
    fun `get meme command cannot be instantiated without providing name's argument`() {
        //when & then
        assertThatThrownBy { GetMemeCommand(memeRepository, TEST_SERVER_ID, mapOf(), env) }
            .isInstanceOf(ArgumentNotPresentException::class.java)
    }

    @Test
    fun `get meme command returns meme url from repository as a public message`() {
        //given
        every { memeRepository.getMemeUrlByName(TEST_SERVER_ID, TEST_MEME_NAME) } returns TEST_MEME_URL
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).isEqualTo(TEST_MEME_URL)
        assertThat(result.isPublic).isTrue
    }

    @Test
    fun `get meme command returns 'no meme' url as a public message when meme is not present in repository`() {
        //given
        every { memeRepository.getMemeUrlByName(TEST_SERVER_ID, TEST_MEME_NAME) } returns null
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).isEqualTo("$memerUrl/img/nomeme.jpg")
        assertThat(result.isPublic).isTrue
    }
}