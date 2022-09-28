package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.repository.MemeRepository
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val memeRepository: MemeRepository,
    private val env: Env
) {
    fun getCommand(context: CommandContext) = when (context.command[0]) {
        "meme" -> executeMemeCommand(context)
        "help" -> HelpCommand()
        else -> UnknownCommand()
    }


    private fun executeMemeCommand(context: CommandContext) = when (context.command[1]) {
        "get" -> GetMemeCommand(memeRepository, context.serverId, context.args, env)
        "add" -> AddMemeCommand(memeRepository, context.serverId, context.args)
        "delete" -> DeleteMemeCommand(memeRepository, context.serverId, context.args)
        "update" -> UpdateMemeCommand(memeRepository, context.serverId, context.args)
        "ranking" -> MemeRankingCommand(memeRepository, context.serverId)
        "list" -> ListMemeCommand(memeRepository, context.serverId, env)
        else -> UnknownCommand()
    }
}