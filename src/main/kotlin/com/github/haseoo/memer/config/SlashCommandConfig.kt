package com.github.haseoo.memer.config

import com.github.haseoo.memer.command.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

private const val MEME_NAME_PARAM_DESCRIPTION = "meme name"
private const val MEME_URL_PARAM_DESCRIPTION = "meme url"

fun initSlashCommands(jda: JDA) {
    jda.upsertCommand(MEME_COMMAND, "Memes on the server")
        .addSubcommands(
            SubcommandData(MEME_ADD_COMMAND, "adds meme")
                .addOption(OptionType.STRING, MEME_NAME_ARG, MEME_NAME_PARAM_DESCRIPTION, true)
                .addOption(OptionType.STRING, MEME_URL_ARG, MEME_URL_PARAM_DESCRIPTION, true)
        )
        .addSubcommands(
            SubcommandData(MEME_UPDATE_COMMAND, "updates meme")
                .addOption(OptionType.STRING, MEME_NAME_ARG, MEME_NAME_PARAM_DESCRIPTION, true)
                .addOption(OptionType.STRING, MEME_URL_ARG, MEME_URL_PARAM_DESCRIPTION, true)
        )
        .addSubcommands(
            SubcommandData(MEME_GET_COMMAND, "posts meme on the current channel")
                .addOption(OptionType.STRING, MEME_NAME_ARG, MEME_NAME_PARAM_DESCRIPTION, true)
        )
        .addSubcommands(
            SubcommandData(MEME_DELETE_COMMAND, "deletes meme")
                .addOption(OptionType.STRING, MEME_NAME_ARG, MEME_NAME_PARAM_DESCRIPTION, true)
        )
        .addSubcommands(SubcommandData(MEME_RANKING_COMMAND, "shows top 10 memes on the server"))
        .addSubcommands(SubcommandData(MEME_LIST_COMMAND, "shows all memes on the server"))
        .queue()
    jda.upsertCommand(HELP_COMMAND, "get help!")
        .queue()
}