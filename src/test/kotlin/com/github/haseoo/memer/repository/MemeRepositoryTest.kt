package com.github.haseoo.memer.repository

import com.github.haseoo.memer.domain.COUNT_MEME_RECORD_FIELD_NAME
import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.domain.NAME_MEME_RECORD_FIELD_NAME
import com.github.haseoo.memer.domain.URL_MEME_RECORD_FIELD_NAME
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import redis.embedded.RedisServer

@SpringBootTest
@ActiveProfiles("test")
internal class MemeRepositoryTest {

    @Autowired
    private lateinit var sut: MemeRepository

    @Autowired
    private lateinit var redisClient: RedisClient

    private lateinit var redisCommands: RedisCommands<String, String>

    @BeforeEach
    fun beforeEach() {
        redisCommands = redisClient.connect().sync()
        redisCommands.flushall()
    }

    @AfterEach
        fun afterEach() {
        redisCommands.flushall()
    }

    companion object {
        @JvmStatic
        private lateinit var redisServer: RedisServer

        @BeforeAll
        @JvmStatic
        private fun init() {
            redisServer = RedisServer(2137)
            redisServer.start()
        }

        @AfterAll
        @JvmStatic
        private fun destroy() {
            redisServer.stop()
        }
    }

    @Test
    fun `get meme by name return meme url if exists for server and increases count by one`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        val memeId = "meme:$serverId:${meme.name}"
        redisCommands.hmset(memeId, meme.toMap())
        //when
        val result = sut.getMemeUrlByName(serverId, meme.name)
        //then
        assertThat(result).isEqualTo(meme.url)
        assertThat(redisCommands.hget(memeId, COUNT_MEME_RECORD_FIELD_NAME)).isEqualTo((meme.count + 1).toString())

    }

    @Test
    fun `get meme by name return null if meme doesn't exist for server`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        //when
        val result = sut.getMemeUrlByName(serverId, meme.name)
        //then
        assertThat(result).isNull()
    }

    @Test
    fun `get memes returns all meme names from server`() {
        //given
        val serverId = 2137L
        val meme1 = Meme("testName1", "testURL", 2137)
        val meme2 =  Meme("testName2", "testURL", 2137)
        redisCommands.hmset("meme:$serverId:${meme1.name}", meme1.toMap())
        redisCommands.hmset("meme:$serverId:${meme2.name}", meme2.toMap())
        //when
        val result = sut.getMemeNames(serverId)
        //then
        assertThat(result).contains(meme1.name, meme2.name)
    }

    @Test
    fun `get memes returns all memes from server`() {
        //given
        val serverId = 2137L
        val meme1 = Meme("testName1", "testURL", 2137)
        val meme2 =  Meme("testName2", "testURL", 2137)
        redisCommands.hmset("meme:$serverId:${meme1.name}", meme1.toMap())
        redisCommands.hmset("meme:$serverId:${meme2.name}", meme2.toMap())
        //when
        val result = sut.getMemes(serverId)
        //then
        assertThat(result).contains(meme1, meme2)
    }

    @Test
    fun `add meme ads meme record and returns true`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        val memeId = "meme:$serverId:${meme.name}"
        //when
        val result = sut.addMeme(serverId, meme.name, meme.url)
        //then
        assertThat(result).isTrue
        assertThat(redisCommands.hget(memeId, NAME_MEME_RECORD_FIELD_NAME)).isEqualTo(meme.name)
        assertThat(redisCommands.hget(memeId, URL_MEME_RECORD_FIELD_NAME)).isEqualTo(meme.url)
        assertThat(redisCommands.hget(memeId, COUNT_MEME_RECORD_FIELD_NAME)).isEqualTo("0")
    }

    @Test
    fun `add meme returns false when record exists`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        val memeId = "meme:$serverId:${meme.name}"
        redisCommands.hmset(memeId, meme.toMap())
        //when & then
        assertThat(sut.addMeme(serverId, meme.name, meme.url)).isFalse
    }

    @Test
    fun `update meme returns false when record does not exist`() {
        //given
        val serverId = 2137L
        //when & then
        assertThat(sut.updateMeme(serverId, "not exist", "doesn't matter")).isFalse
    }

    @Test
    fun `update meme updates record returns true when record exists`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        val memeId = "meme:$serverId:${meme.name}"
        redisCommands.hmset(memeId, meme.toMap())
        //when
        val newMemeUrl = "new url"
        val result = sut.updateMeme(serverId, meme.name, newMemeUrl)
        //then
        assertThat(result).isTrue
        assertThat(redisCommands.hget(memeId, NAME_MEME_RECORD_FIELD_NAME)).isEqualTo(meme.name)
        assertThat(redisCommands.hget(memeId, URL_MEME_RECORD_FIELD_NAME)).isEqualTo(newMemeUrl)
        assertThat(redisCommands.hget(memeId, COUNT_MEME_RECORD_FIELD_NAME)).isEqualTo(meme.count.toString())
    }

    @Test
    fun `delete meme returns true when meme was deleted`() {
        //given
        val serverId = 2137L
        val meme = Meme("testName1", "testURL", 2137)
        redisCommands.hmset("meme:$serverId:${meme.name}", meme.toMap())
        //when & then
        assertThat(sut.deleteMeme(serverId, meme.name)).isTrue
    }

    @Test
    fun `delete meme returns false when meme was not deleted`() {
        //given
        val serverId = 2137L
        //when & then
        assertThat(sut.deleteMeme(serverId, "doesNotExist")).isFalse
    }

    @Test
    fun `get ranking returns only 10 memes with largest count in count order`() {
        //given
        val serverId = 2137L
        val memes = (15 downTo 0)
            .map { Meme("meme$it", "urlOfMeme", it) }
            .toList()
        memes.shuffled()
            .forEach { redisCommands.hmset("meme:$serverId:${it.name}", it.toMap()) }
        //when
        val result = sut.getRanking(serverId)
        //then
        assertThat(result).containsOnlyOnceElementsOf(memes.subList(0, 10))
        assertThat(result).containsSequence(memes.subList(0, 10))
        assertThat(result).doesNotContainAnyElementsOf(memes.subList(10, 16))
    }

}