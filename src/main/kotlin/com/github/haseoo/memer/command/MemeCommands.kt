package com.github.haseoo.memer.command

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.repository.MemeRepository
import org.apache.commons.validator.routines.UrlValidator

private const val MEME_NAME_ARG = "name"
private const val MEME_URL_ARG = "url"
private val urlValidator = UrlValidator()
private fun requireValidUrl(args: Map<String, String>) {
    require(urlValidator.isValid(args[MEME_URL_ARG]!!)) { "Provided url is not valid." }
}
abstract class BaseMemeCommand(
    protected val memeRepository: MemeRepository,
    protected val serverId: Long,
    args: Map<String, String>,
    vararg argNames: String
): Command {
    init {
        validateCommandArguments(args, *argNames)
    }
}

class GetMemeCommand (
    memeRepository: MemeRepository,
    serverId: Long,
    args: Map<String, String>,
    env: Env
        ) : BaseMemeCommand(memeRepository, serverId, args, MEME_NAME_ARG) {

    private val memeName: String  = args[MEME_NAME_ARG]!!
    private val defaultMemeUrl = env.memerUrl + "/img/nomeme.jpg"

    override fun execute() =
        CommandResult(memeRepository.getMemeUrlByName(serverId, memeName) ?: defaultMemeUrl, true)
}

class AddMemeCommand (
    memeRepository: MemeRepository,
    serverId: Long,
    args: Map<String, String>,
) : BaseMemeCommand(memeRepository, serverId, args, MEME_NAME_ARG, MEME_URL_ARG) {

    private val memeName: String
    private val memeUrl: String
    init {
        memeName = args[MEME_NAME_ARG]!!
        requireValidUrl(args)
        memeUrl = args[MEME_URL_ARG]!!
    }

    override fun execute() = CommandResult(
        if (memeRepository.addMeme(serverId, memeName, memeUrl)) "Added"
            else "Not added, meme already exists or an error has occurred",
        false
    )
}

class UpdateMemeCommand (
    memeRepository: MemeRepository,
    serverId: Long,
    args: Map<String, String>,
) : BaseMemeCommand(memeRepository, serverId, args, MEME_NAME_ARG, MEME_URL_ARG) {

    private val memeName: String
    private val memeUrl: String
    init {
        memeName = args[MEME_NAME_ARG]!!
        require(!urlValidator.isValid(args[MEME_URL_ARG]!!)) { "Provided url is not valid" }
        memeUrl = args[MEME_URL_ARG]!!
    }
    override fun execute() = CommandResult(
        if (memeRepository.updateMeme(serverId, memeName, memeUrl)) "Updated"
        else "Not updated, meme don't exist or an error has occurred",
        false
    )
}

class DeleteMemeCommand (
    memeRepository: MemeRepository,
    serverId: Long,
    args: Map<String, String>,
) : BaseMemeCommand(memeRepository, serverId, args, MEME_NAME_ARG) {

    private val memeName  = args[MEME_NAME_ARG]!!
    override fun execute() = CommandResult(
        if (memeRepository.deleteMeme(serverId, memeName)) "Deleted"
        else "Not deleted, meme don't exist or an error has occurred",
        false
    )
}

class RankingMemeCommand (
    private val memeRepository: MemeRepository,
    private val serverId: Long): Command {
    override fun execute(): CommandResult = CommandResult(
        memeRepository.getRanking(serverId)
            .mapIndexed{ ranking, meme -> "#${ranking + 1}: ${meme.name}\n" }
            .joinToString(),
        false
    )
}

class ListMemeCommand (
    private val memeRepository: MemeRepository,
    private val serverId: Long,
    env: Env): Command {

    private val memesUrl = env.memerUrl + "/memes/$serverId"
    override fun execute(): CommandResult = CommandResult(
        memeRepository.getMemes(serverId).joinToString(
            ", ",
            "Meme list on the sever: ",
            ".\nYou can see all memes here <$memesUrl>."
        ) { it.name },
        false
    )
}