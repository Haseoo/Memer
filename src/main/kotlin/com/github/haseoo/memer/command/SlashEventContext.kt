package com.github.haseoo.memer.command

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class SlashEventContext(event: SlashCommandEvent) {

    val serverId = event.guild!!.idLong

    val command = event.commandPath.split("/")

    val args = event.options.associate { it.name to it.asString }

}