package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.repository.MemeRepository
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val memeRepository: MemeRepository,
    private val env: Env
) {
    fun execute(context: SlashEventContext) = when {
        context.command[0] == "meme" -> executeMemeCommand(context)
        else -> UnknownCommand()
    }


    private fun executeMemeCommand(context: SlashEventContext) = when {
        context.command[1] == "get" -> GetMemeCommand(memeRepository, env, context.serverId, context.args["name"]!!)
        context.command[1] == "add" -> AddMemeCommand(memeRepository, context.serverId, context.args["name"]!!, context.args["url"]!!)
        context.command[1] == "delete" -> DeleteMemeCommand(memeRepository, context.serverId, context.args["name"]!!)
        context.command[1] == "update" -> UpdateMemeCommand(memeRepository, context.serverId, context.args["name"]!!, context.args["url"]!!)
        else -> UnknownCommand()
    }
}