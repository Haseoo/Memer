package com.github.haseoo.memer.repository

import com.github.haseoo.memer.domain.COUNT_MEME_RECORD_FIELD_NAME
import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.domain.NAME_MEME_RECORD_FIELD_NAME
import com.github.haseoo.memer.domain.URL_MEME_RECORD_FIELD_NAME
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Repository
import java.util.SortedSet


@Repository
class MemeRepository (redisClient: RedisClient) {

    private val _redisCommand: RedisCommands<String, String>

    init {
        _redisCommand = redisClient.connect().sync()
    }

    fun getMemeUrlByName(serverId: Long, name: String): String? {
        val memeId = getMemeId(serverId, name)
        if((_redisCommand.exists(memeId) ?: 0) == 0L) {
            return null
        }
        _redisCommand.hincrby(memeId, COUNT_MEME_RECORD_FIELD_NAME, 1)
        return _redisCommand.hget(memeId, URL_MEME_RECORD_FIELD_NAME)
    }

    fun getRanking(serverId: Long): Collection<Meme> =
        getMemesKeys(serverId)
            .map {_redisCommand.hgetall(it) }
            .map { Meme(it) }
            .sortedByDescending { it.count }
            .take(10)

    fun getMemes(serverId: Long): Collection<Meme> =
        getMemesKeys(serverId)
            .map {_redisCommand.hgetall(it) }
            .map { Meme(it) }

    fun getMemeNames(serverId: Long): SortedSet<String> =
        getMemesKeys(serverId)
            .map {_redisCommand.hget(it, NAME_MEME_RECORD_FIELD_NAME) }
            .toSortedSet()

    fun addMeme(serverId: Long, memeName: String, memeUrl: String): Boolean {
        val memeId = getMemeId(serverId, memeName)
        if(_redisCommand.exists(memeId) != 0L) {
            return false
        }
        return _redisCommand.hmset(memeId, Meme(memeName, memeUrl, 0).toMap()) == "OK"
    }

    fun deleteMeme(serverId: Long, memeName: String) = _redisCommand.del(getMemeId(serverId, memeName)) != 0L

    fun updateMeme(serverId: Long, memeName: String, memeUrl: String): Boolean {
        val memeId = getMemeId(serverId, memeName)
        if(_redisCommand.exists(memeId) == 0L) {
            return false
        }
        _redisCommand.hset(memeId, URL_MEME_RECORD_FIELD_NAME, memeUrl)
        return true
    }

    private fun getMemesKeys(serverId: Long): Set<String> = _redisCommand.keys("meme:$serverId:*").toSet()

    private fun getMemeId(serverId: Long, memeName: String) = "meme:$serverId:$memeName"
}