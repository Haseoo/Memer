package com.github.haseoo.memer.command

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HelpCommandTest {
    private lateinit var sut: HelpCommand

    @BeforeEach
    fun init() {
        sut = HelpCommand()
    }

    @Test
    fun `help command sends private message`() {
        //when & then
        Assertions.assertThat(sut.execute().isPublic).isFalse
    }

    @Test
    fun `help command contains information about memes command`() {
        //when & then
        Assertions.assertThat(sut.execute().replyMessage).containsIgnoringCase("/meme")
    }
}