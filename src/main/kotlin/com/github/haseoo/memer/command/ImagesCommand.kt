package com.github.haseoo.memer.command

import com.github.haseoo.memer.service.ImageService

class ImagesCommand(
    private val serverId: Long,
    private val serverName: String,
    private val allowedChannelIds: Collection<Long>,
    private val _imageService: ImageService
) : Command {

    override fun execute(): CommandResult {
        val url = _imageService.generateImageLink(serverId, serverName, allowedChannelIds)
        return CommandResult("Here's single use link that will expire in 5 minutes: <$url>", false)
    }
}