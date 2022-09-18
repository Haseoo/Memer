package com.github.haseoo.memer.domain.exception

class ArgumentNotPresentException(argumentName: String):
    IllegalArgumentException("Argument $argumentName not provided.")