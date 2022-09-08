package com.github.haseoo.memer.config

import io.lettuce.core.RedisClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfiguration {

    @Bean
    fun redisClient(env: Env): RedisClient = RedisClient.create(env.redisUri)

}