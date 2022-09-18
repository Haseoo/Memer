package com.github.haseoo.memer

import com.github.haseoo.memer.command.CommandContext
import io.mockk.every
import io.mockk.mockk

const val TEST_MEME_NAME = "testName"

const val TEST_MEME_URL = "http://www.example.com"

fun generateCommandContext(command: List<String>, args: Map<String, String>): CommandContext {
    val mock = mockk<CommandContext>()
    every { mock.command } returns command.toList()
    every { mock.serverId } returns 2137L
    every { mock.args } returns args
    return mock
}

fun generateCommandContext(command: List<String>): CommandContext {
    return generateCommandContext(command, mapOf("name" to TEST_MEME_NAME, "url" to TEST_MEME_URL))
}