package com.github.haseoo.memer.listener

import com.github.haseoo.memer.command.CommandContext
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

data class SlashEventContext(
    override val serverId: Long,
    override val command: List<String>,
    override val args: Map<String, String>
) : CommandContext {
    constructor(event: SlashCommandEvent) : this(event.guild!!.idLong,
        event.commandPath.split("/"),
        event.options.associate { it.name to it.asString })

}