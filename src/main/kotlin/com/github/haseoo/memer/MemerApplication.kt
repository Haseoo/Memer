package com.github.haseoo.memer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MemerApplication

fun main(args: Array<String>) {
	runApplication<MemerApplication>(*args)
}
