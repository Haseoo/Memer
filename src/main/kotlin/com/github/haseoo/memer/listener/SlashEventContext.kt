package com.github.haseoo.memer.listener

import com.github.haseoo.memer.command.CommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

data class SlashEventContext(
    override val serverId: Long,
    override val serverName: String,
    override val userAllowedChannelIds: Collection<Long>,
    override val command: List<String>,
    override val args: Map<String, String>,

    ) : CommandContext {
    constructor(event: SlashCommandEvent) : this(
        event.guild!!.idLong,
        event.guild!!.name,
        getPermittedChannels(event),
        event.commandPath.split("/"),
        event.options.associate { it.name to it.asString })
}

private fun getPermittedChannels(event: SlashCommandEvent) = event.guild!!
    .textChannels.filter {
        event.member!!
            .getPermissions(it!!)
            .contains(Permission.MESSAGE_READ)
    }
    .map { it.idLong }
    .toSet()