package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.repository.MemeRepository

class GetMemeCommand (
    private val memeRepository: MemeRepository,
    env: Env,
    private val serverId: Long,
    private val memeName: String
        ) : Command {
    override fun execute() =
        CommandResult(memeRepository.getMemeUrlByName(serverId, memeName) ?: defaultMemeUrl, true)

    private val defaultMemeUrl = env.memerUrl + "/img/nomeme.jpg"
}

class AddMemeCommand (
    private val memeRepository: MemeRepository,
    private val serverId: Long,
    private val memeName: String,
    private val memeUrl: String
        ) : Command {
    override fun execute() = CommandResult(
        if (memeRepository.addMeme(serverId, memeName, memeUrl)) "Added"
            else "Not added, meme already exists or an error has occurred",
        false
    )
}

class UpdateMemeCommand (
    private val memeRepository: MemeRepository,
    private val serverId: Long,
    private val memeName: String,
    private val memeUrl: String
) : Command {
    override fun execute() = CommandResult(
        if (memeRepository.updateMeme(serverId, memeName, memeUrl)) "Updated"
        else "Not updated, meme don't exist or an error has occurred",
        false
    )
}

class DeleteMemeCommand (
    private val memeRepository: MemeRepository,
    private val serverId: Long,
    private val memeName: String
) : Command {
    override fun execute() = CommandResult(
        if (memeRepository.deleteMeme(serverId, memeName)) "Deleted"
        else "Not deleted, meme don't exist or an error has occurred",
        false
    )
}