package com.github.haseoo.memer.command

import com.github.haseoo.memer.TEST_MEME_URL
import com.github.haseoo.memer.TEST_SERVER_ID
import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.repository.MemeRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ListMemeCommandTest {

    private val memerUrl = "http://test.example.com"

    private lateinit var memeRepository: MemeRepository

    private lateinit var env: Env

    private lateinit var sut: ListMemeCommand

    @BeforeEach
    fun setUp() {
        memeRepository = mockk()
        env = mockk()
        every { env.memerUrl } returns memerUrl
        sut = ListMemeCommand(memeRepository, TEST_SERVER_ID, env)
    }

    @Test
    fun `list meme command returns meme names for server with link to page that displays it as a private message`() {
        //given
        every { memeRepository.getMemes(TEST_SERVER_ID) } returns listOf(
            Meme("meme1", TEST_MEME_URL, 0),
            Meme("meme2", TEST_MEME_URL, 0),
            Meme("meme3", TEST_MEME_URL, 0),
            Meme("meme4", TEST_MEME_URL, 0),
            Meme("meme5", TEST_MEME_URL, 0),
        )
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).containsIgnoringCase("meme1")
            .containsIgnoringCase("meme2")
            .containsIgnoringCase("meme3")
            .containsIgnoringCase("meme4")
            .containsIgnoringCase("meme5")
            .containsIgnoringCase("$memerUrl/memes/$TEST_SERVER_ID")
        assertThat(result.isPublic).isFalse
    }
}