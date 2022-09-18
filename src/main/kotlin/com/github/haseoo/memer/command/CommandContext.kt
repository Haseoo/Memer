package com.github.haseoo.memer.command

interface CommandContext {
    val serverId: Long
    val command: List<String>
    val args: Map<String, String>
}