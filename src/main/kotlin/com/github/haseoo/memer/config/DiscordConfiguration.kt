package com.github.haseoo.memer.config

import com.github.haseoo.memer.listener.SlashCommandListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener


@Configuration
class DiscordConfiguration(private val env: Env, private val slashCommandListener: SlashCommandListener) {
    private var jda: JDA? = null

    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {
        jda = JDABuilder.createDefault(env.botToken)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .addEventListeners(slashCommandListener)
            .build()
        jda!!.presence.activity = Activity.playing("Your mom")
    }

    @EventListener(ContextClosedEvent::class)
    fun contextStoppedEvent() {
        if (jda != null) {
            jda!!.shutdown()
            jda = null
        }
    }
}