package com.github.haseoo.memer.command

class HelpCommand: Command {
    override fun execute() = CommandResult("""
        You can use following commands:
        /meme get/add/update/delete - for managing memes on the server
    """.trimIndent(),false)
}