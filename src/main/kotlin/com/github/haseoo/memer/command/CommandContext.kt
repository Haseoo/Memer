package com.github.haseoo.memer.command

interface CommandContext {
    val serverId: Long
    val serverName: String
    val userAllowedChannelIds: Collection<Long>
    val command: List<String>
    val args: Map<String, String>
}