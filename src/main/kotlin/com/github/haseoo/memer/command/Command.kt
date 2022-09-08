package com.github.haseoo.memer.command

import org.springframework.stereotype.Service

@Service
interface Command {
    fun execute(params: Map<String, String>): CommandResult
}