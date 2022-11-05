package com.github.haseoo.memer.listener

import com.github.haseoo.memer.command.Command
import com.github.haseoo.memer.command.CommandResult
import com.github.haseoo.memer.command.CommandService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class SlashCommandListenerTest {

    @MockK
    private lateinit var jda: JDA

    @MockK
    private lateinit var commandService: CommandService

    @InjectMockKs
    private lateinit var sut: SlashCommandListener


    @Test
    fun `test slash command handling`() {
        //before
        val serverId = 2137L
        val serverName = "Super"
        val chanellId = 69L
        val commandParts = listOf("foo", "bar")
        val args = mapOf("foo" to "bar")

        val commandResult = CommandResult("test message", true)
        val commandMock = mockk<Command>()
        val eventMock = mockk<SlashCommandEvent>()
        val guidMock = mockk<Guild>()
        val optionMappingMock = mockk<OptionMapping>()
        val replyActionMock = mockk<ReplyAction>()
        val deferReplyActionMock = mockk<ReplyAction>()
        val textChannelMock = mockk<TextChannel>()
        val memberMock = mockk<Member>()
        every { optionMappingMock.name } returns "foo"
        every { optionMappingMock.asString } returns "bar"
        every { guidMock.idLong } returns serverId
        every { guidMock.name } returns serverName
        every { guidMock.textChannels } returns listOf(textChannelMock)
        every { eventMock.guild } returns guidMock
        every { eventMock.commandPath } returns "foo/bar"
        every { eventMock.options } returns listOf(optionMappingMock)
        every { eventMock.deferReply() } returns deferReplyActionMock
        justRun { deferReplyActionMock.queue() }
        every { commandMock.execute() } returns commandResult
        every { eventMock.reply(commandResult.replyMessage) } returns replyActionMock
        every { eventMock.member } returns memberMock
        every { replyActionMock.setEphemeral(!commandResult.isPublic) } returns replyActionMock
        every { memberMock.getPermissions(textChannelMock) } returns EnumSet.of(Permission.MESSAGE_READ)
        every { textChannelMock.idLong } returns chanellId
        justRun { replyActionMock.queue() }
        val expectedCommandContext = SlashEventContext(serverId, serverName, setOf(chanellId), commandParts, args)

        //scenario: handler defers reply, executes command and uses result to reply to the event
        //given
        every { commandService.getCommand(expectedCommandContext) } returns commandMock
        //when
        sut.onSlashCommand(eventMock)
        //then
        verify { eventMock.deferReply() }
        verify { replyActionMock.queue() }

        //scenario: when unexpected exception occurs handler replies with defined error message as private message
        //given
        every { commandMock.execute() } throws Exception("I'm so unexpected, yet expected!")
        every { eventMock.reply(any<String>()) } returns replyActionMock
        every { replyActionMock.setEphemeral(true) } returns replyActionMock
        //when
        sut.onSlashCommand(eventMock)
        //then
        verify { eventMock.reply(withArg<String> { it.contains("unexpected exception", true) }) }
        verify { replyActionMock.queue() }

        //scenario: when illegal argument exception occurs handler replies with exception message as private message
        //given
        val message = "invalid arg jp2 not found"
        every { commandMock.execute() } throws IllegalArgumentException(message)
        every { eventMock.reply(any<String>()) } returns replyActionMock
        every { replyActionMock.setEphemeral(true) } returns replyActionMock
        //when
        sut.onSlashCommand(eventMock)
        //then
        verify { eventMock.reply(message) }
        verify { replyActionMock.queue() }
    }

}