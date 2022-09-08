package com.github.haseoo.memer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemerApplication

fun main(args: Array<String>) {
	runApplication<MemerApplication>(*args)
}
