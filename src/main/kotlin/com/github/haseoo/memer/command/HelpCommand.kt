package com.github.haseoo.memer.command

class HelpCommand: Command {
    override fun execute() = CommandResult("""
        You can use following commands:
        /meme get/add/update/delete - for managing memes on the server
        /images - sends you a link to the page with images sent on this server grouped by channels; the link expires in 5 minutes and it's single-use
    """.trimIndent(),false)
}