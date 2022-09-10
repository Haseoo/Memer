package com.github.haseoo.memer.listener

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Service

@Service
@DiscordListener
class SlashCommandListener : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        event.deferReply()
        if (event.name != "meme") {
            return
        }
        println(event.name)
        event.options.forEach { println("${it.name} : ${it.asString}") }
        event.reply("https://i.kym-cdn.com/photos/images/newsfeed/001/550/907/d41.jpg").setEphemeral(false).queue()
    }
}