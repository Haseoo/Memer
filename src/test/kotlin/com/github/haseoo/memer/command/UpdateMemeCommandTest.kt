package com.github.haseoo.memer.command

import com.github.haseoo.memer.COMMAND_ARGS
import com.github.haseoo.memer.TEST_MEME_NAME
import com.github.haseoo.memer.TEST_MEME_URL
import com.github.haseoo.memer.TEST_SERVER_ID
import com.github.haseoo.memer.domain.exception.ArgumentNotPresentException
import com.github.haseoo.memer.repository.MemeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UpdateMemeCommandTest {


    private lateinit var memeRepository: MemeRepository

    private lateinit var sut: UpdateMemeCommand

    @BeforeEach
    fun setUp() {
        memeRepository = mockk()
        sut = UpdateMemeCommand(memeRepository, TEST_SERVER_ID, COMMAND_ARGS)
    }

    @Test
    fun `update meme command cannot be instantiated without providing 'name' argument`() {
        //when & then
        assertThatThrownBy { UpdateMemeCommand(memeRepository, TEST_SERVER_ID, mapOf("url" to TEST_MEME_URL)) }
            .isInstanceOf(ArgumentNotPresentException::class.java)
    }

    @Test
    fun `update meme command cannot be instantiated without providing 'url' argument`() {
        //when & then
        assertThatThrownBy { UpdateMemeCommand(memeRepository, TEST_SERVER_ID, mapOf("name" to TEST_MEME_NAME)) }
            .isInstanceOf(ArgumentNotPresentException::class.java)
    }

    @Test
    fun `update meme command cannot be instantiated with malformed 'url' argument`() {
        //when & then
        assertThatThrownBy {
            UpdateMemeCommand(
                memeRepository, TEST_SERVER_ID, mapOf(
                    "name" to TEST_MEME_NAME,
                    "url" to "this is not a url"
                )
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("url is not valid")
    }

    @Test
    fun `update meme command updates meme to repository and returns 'Updated' as a private message when repository returns true`() {
        //given
        every { memeRepository.updateMeme(TEST_SERVER_ID, TEST_MEME_NAME, TEST_MEME_URL) } returns true
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).isEqualTo("Updated")
        assertThat(result.isPublic).isFalse
    }

    @Test
    fun `update meme command updates meme to repository and returns error message as a private message when repository returns false`() {
        //given
        every { memeRepository.updateMeme(TEST_SERVER_ID, TEST_MEME_NAME, TEST_MEME_URL) } returns false
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).containsIgnoringCase("not updated")
        assertThat(result.isPublic).isFalse
        verify { memeRepository.updateMeme(TEST_SERVER_ID, TEST_MEME_NAME, TEST_MEME_URL) }
    }
}