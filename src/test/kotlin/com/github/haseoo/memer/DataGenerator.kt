package com.github.haseoo.memer

import com.github.haseoo.memer.command.CommandContext
import io.mockk.every
import io.mockk.mockk

const val TEST_MEME_NAME = "testName"

const val TEST_MEME_URL = "http://www.example.com"

val COMMAND_ARGS = mapOf("name" to TEST_MEME_NAME, "url" to TEST_MEME_URL)

const val TEST_SERVER_ID = 2137L

fun generateCommandContext(command: List<String>): CommandContext {
    val mock = mockk<CommandContext>()
    every { mock.command } returns command.toList()
    every { mock.serverId } returns TEST_SERVER_ID
    every { mock.args } returns COMMAND_ARGS
    return mock
}