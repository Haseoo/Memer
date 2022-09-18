package com.github.haseoo.memer.listener

import com.github.haseoo.memer.command.CommandResult
import com.github.haseoo.memer.command.CommandService
import com.github.haseoo.memer.command.SlashEventContext
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Service

@Service
@DiscordListener
class SlashCommandListener(private val commandService: CommandService) : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        super.onSlashCommand(event)
        event.deferReply()
        val result = try {
            commandService.execute(SlashEventContext(event)).execute()
        } catch (e: IllegalArgumentException) {
            CommandResult(e.message ?: "Bad arguments", false)
        }
        catch (e: Exception) {
            e.printStackTrace()
            CommandResult("Oops, an exception has occurred, if it persist contact me on github (@Haseoo)", false)
        }
        event.reply(result.replyMessage).setEphemeral(!result.isPublic).queue()
    }
}