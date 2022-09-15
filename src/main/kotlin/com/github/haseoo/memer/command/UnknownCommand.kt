package com.github.haseoo.memer.command

class UnknownCommand: Command {
    override fun execute(): CommandResult = CommandResult("Sorry, unknown command", false)
}