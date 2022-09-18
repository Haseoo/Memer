package com.github.haseoo.memer.listener

import com.github.haseoo.memer.command.CommandContext
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class SlashEventContext(event: SlashCommandEvent) : CommandContext {

    override val serverId = event.guild!!.idLong

    override val command = event.commandPath.split("/")

    override val args = event.options.associate { it.name to it.asString }

}