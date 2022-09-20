package com.github.haseoo.memer.command

import com.github.haseoo.memer.COMMAND_ARGS
import com.github.haseoo.memer.TEST_MEME_NAME
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

internal class DeleteMemeCommandTest {


    private lateinit var memeRepository: MemeRepository

    private lateinit var sut: DeleteMemeCommand

    @BeforeEach
    fun setUp() {
        memeRepository = mockk()
        sut = DeleteMemeCommand(memeRepository, TEST_SERVER_ID, COMMAND_ARGS)
    }

    @Test
    fun `delete meme command cannot be instantiated without providing name's argument`() {
        //when & then
        assertThatThrownBy { DeleteMemeCommand(memeRepository, TEST_SERVER_ID, mapOf()) }
            .isInstanceOf(ArgumentNotPresentException::class.java)
    }

    @Test
    fun `delete meme command deletes meme form repository and returns 'Deleted' as a private message when repository returns true`() {
        //given
        every { memeRepository.deleteMeme(TEST_SERVER_ID, TEST_MEME_NAME) } returns true
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).isEqualTo("Deleted")
        assertThat(result.isPublic).isFalse
    }

    @Test
    fun `delete meme command deletes meme form repository and returns error message as a private message when repository returns false`() {
        //given
        every { memeRepository.deleteMeme(TEST_SERVER_ID, TEST_MEME_NAME) } returns false
        //when
        val result = sut.execute()
        //then
        assertThat(result.replyMessage).containsIgnoringCase("not deleted")
        assertThat(result.isPublic).isFalse
        verify { memeRepository.deleteMeme(TEST_SERVER_ID, TEST_MEME_NAME) }
    }
}