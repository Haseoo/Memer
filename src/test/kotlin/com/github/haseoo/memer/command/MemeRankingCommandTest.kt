package com.github.haseoo.memer.command

import com.github.haseoo.memer.TEST_MEME_URL
import com.github.haseoo.memer.TEST_SERVER_ID
import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.repository.MemeRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MemeRankingCommandTest {

    private lateinit var memeRepository: MemeRepository

    private lateinit var sut: MemeRankingCommand

    @BeforeEach
    fun setUp() {
        memeRepository = mockk()
        sut = MemeRankingCommand(memeRepository, TEST_SERVER_ID)
    }

    @Test
    fun `meme ranking command returns meme names for server with link to page that displays it as a private message`() {
        //given
        every { memeRepository.getRanking(TEST_SERVER_ID) } returns listOf(
            Meme("meme1", TEST_MEME_URL, 5),
            Meme("meme2", TEST_MEME_URL, 4),
            Meme("meme3", TEST_MEME_URL, 3),
            Meme("meme4", TEST_MEME_URL, 2),
            Meme("meme5", TEST_MEME_URL, 1),
        )
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).containsIgnoringCase("#1: meme1 used 5 times")
            .containsIgnoringCase("#2: meme2 used 4 times")
            .containsIgnoringCase("#3: meme3 used 3 times")
            .containsIgnoringCase("#4: meme4 used 2 times")
            .containsIgnoringCase("#5: meme5 used 1 times")
        assertThat(result.isPublic).isFalse
    }
}