package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.repository.MemeRepository
import org.springframework.stereotype.Service

const val MEME_COMMAND = "meme"
const val HELP_COMMAND = "help"

const val MEME_GET_COMMAND = "get"
const val MEME_ADD_COMMAND = "add"
const val MEME_DELETE_COMMAND = "delete"
const val MEME_UPDATE_COMMAND = "update"
const val MEME_RANKING_COMMAND = "ranking"
const val MEME_LIST_COMMAND = "list"

@Service
class CommandService(
    private val memeRepository: MemeRepository,
    private val env: Env
) {
    fun getCommand(context: CommandContext) = when (context.command[0]) {
        MEME_COMMAND -> executeMemeCommand(context)
        HELP_COMMAND -> HelpCommand()
        else -> UnknownCommand()
    }

    private fun executeMemeCommand(context: CommandContext) = when (context.command[1]) {
        MEME_GET_COMMAND -> GetMemeCommand(memeRepository, context.serverId, context.args, env)
        MEME_ADD_COMMAND -> AddMemeCommand(memeRepository, context.serverId, context.args)
        MEME_DELETE_COMMAND -> DeleteMemeCommand(memeRepository, context.serverId, context.args)
        MEME_UPDATE_COMMAND -> UpdateMemeCommand(memeRepository, context.serverId, context.args)
        MEME_RANKING_COMMAND -> MemeRankingCommand(memeRepository, context.serverId)
        MEME_LIST_COMMAND -> ListMemeCommand(memeRepository, context.serverId, env)
        else -> UnknownCommand()
    }
}