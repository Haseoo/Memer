package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.generateCommandContext
import com.github.haseoo.memer.repository.MemeRepository
import com.github.haseoo.memer.service.ImageService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class CommandServiceTest {

    @MockK
    private lateinit var memeRepository: MemeRepository

    @MockK
    private lateinit var env: Env

    @MockK
    private lateinit var imageService: ImageService

    @InjectMockKs
    private lateinit var sut: CommandService

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @ParameterizedTest
    @MethodSource("provideCommandTestArgument")
    fun `get command returns command object based on command parts`(
        command: List<String>,
        expectedCommandObjectClass: Class<out Command>
    ) {
        //given
        val commandContext = generateCommandContext(command)
        every { env.memerUrl } returns "http://loclahost:8080/memer"
        //when & then
        assertThat(sut.getCommand(commandContext)).isInstanceOf(expectedCommandObjectClass)
    }

    companion object {
        @JvmStatic
        fun provideCommandTestArgument(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("meme", "get"), GetMemeCommand::class.java),
            Arguments.of(listOf("meme", "add"), AddMemeCommand::class.java),
            Arguments.of(listOf("meme", "update"), UpdateMemeCommand::class.java),
            Arguments.of(listOf("meme", "delete"), DeleteMemeCommand::class.java),
            Arguments.of(listOf("meme", "list"), ListMemeCommand::class.java),
            Arguments.of(listOf("meme", "ranking"), MemeRankingCommand::class.java),
            Arguments.of(listOf("help"), HelpCommand::class.java),
            Arguments.of(listOf("blah blah"), UnknownCommand::class.java),
            Arguments.of(listOf("meme", "blah blah"), UnknownCommand::class.java)
        )
    }

}