package com.github.haseoo.memer.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.haseoo.memer.domain.images.ImagesAuthData
import io.lettuce.core.RedisClient
import io.lettuce.core.SetArgs
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ImagesAuthDataRepository(
    redisClient: RedisClient,
    private val _mapper: ObjectMapper,
) {
    private val _redisCommand: RedisCommands<String, String>

    init {
        _redisCommand = redisClient.connect().sync()
    }

    fun saveAuthData(key: String, imagesAuthData: ImagesAuthData) {
        val dataAsString = _mapper.writeValueAsString(imagesAuthData)
        _redisCommand.set(getAuthDataId(key), dataAsString, SetArgs().ex(Duration.ofMinutes(5)))
    }

    fun popAuthDataAsString(key: String): String? {
        val authDataId = getAuthDataId(key)
        if (_redisCommand.exists(authDataId) == 0L) {
            return null
        }
        val authData = _redisCommand[authDataId]
        _redisCommand.del(authDataId)
        return authData
    }

    private fun getAuthDataId(key: String) = "img:auth:$key"

}