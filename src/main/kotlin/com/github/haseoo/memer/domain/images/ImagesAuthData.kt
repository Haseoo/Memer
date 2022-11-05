package com.github.haseoo.memer.domain.images

data class ImagesAuthData(
    val serverId: Long,
    val serverName: String,
    val channelIds: Collection<Long>
)
