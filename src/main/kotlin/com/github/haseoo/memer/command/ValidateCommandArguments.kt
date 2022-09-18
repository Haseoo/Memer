package com.github.haseoo.memer.command

import com.github.haseoo.memer.domain.exception.ArgumentNotPresentException

fun validateCommandArguments(arguments: Map<String, String>, vararg argumentNames: String) =
    argumentNames.forEach { require(arguments.containsKey(it)) { throw ArgumentNotPresentException(it) } }