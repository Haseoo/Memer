package com.github.haseoo.memer.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "env")
class Env {
    lateinit var redisUri: String
    lateinit var botToken: String
    lateinit var memerUrl: String
    lateinit var dscViewerUrl: String
    lateinit var dscViewerRedirectUrl: String
    lateinit var domain: String
    var isProd: Boolean = false
    lateinit var imageUploadedQueueName: String
    lateinit var messageDeletedQueueName: String
    lateinit var channelUpdatedQueueName: String
}