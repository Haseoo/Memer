package com.github.haseoo.memer.repository

import com.github.haseoo.memer.domain.images.ImagesAuthData
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import redis.embedded.RedisServer

@SpringBootTest
@ActiveProfiles("test")
internal class ImagesAuthDataRepositoryTest {

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

    @Autowired
    private lateinit var sut: ImagesAuthDataRepository

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

    @Test
    fun `saveAuthData should save given auth data in JSON format and with given id`() {
        //given
        val key = "testId"
        val expectedId = "img:auth:$key"
        val authData = ImagesAuthData(2137, "test", listOf(1, 2, 3))
        val expectedAuthDataContent = """{"serverId":2137,"serverName":"test","channelIds":[1,2,3]}"""
        //when
        sut.saveAuthData(key, authData)
        //then
        assertThat(redisCommands[expectedId]).isEqualTo(expectedAuthDataContent)
    }

    @Test
    fun `popAuthDataAsString should return stored auth data and delete it from storage`() {
        //given
        val key = "testId"
        val expectedId = "img:auth:$key"
        val authDataContent = """{"serverId":2137,"serverName":"test","channelIds":[1,2,3]}"""
        redisCommands[expectedId] = authDataContent
        //when
        val result = sut.popAuthData(key)
        //then
        assertThat(result).isEqualTo(authDataContent)
        assertThat(redisCommands.exists(expectedId)).isZero
    }

    @Test
    fun `popAuthDataAsString should return null when auth data is not present in the storage`() {
        //when & then
        assertThat(sut.popAuthData("this does not exist")).isNull()
    }
}