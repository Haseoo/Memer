package com.github.haseoo.memer.config

import io.lettuce.core.RedisClient
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.security.SecureRandom

@Configuration
class BeanConfiguration {

    @Bean
    fun redisClient(env: Env): RedisClient = RedisClient.create(env.redisUri)

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate = restTemplateBuilder.build()

    @Bean
    fun secureRandom() = SecureRandom()
}