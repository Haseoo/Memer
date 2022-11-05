package com.github.haseoo.memer.controller

import com.github.haseoo.memer.controller.view.MemeMediaType
import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.repository.ImagesAuthDataRepository
import com.github.haseoo.memer.repository.MemeRepository
import com.github.haseoo.memer.service.ImageService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.awt.PageAttributes.MediaType
import java.util.stream.Stream

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class MemeListControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var jda: JDA

    @MockkBean
    private lateinit var memeRepository: MemeRepository

    @MockkBean
    private lateinit var imageService: ImageService

    @MockkBean
    private lateinit var imagesAuthDataRepository: ImagesAuthDataRepository

    @Test
    fun `meme list page contains server name retrieved form Discord client`() {
        //given
        val serverId = 2137L
        val serverName = "test server name"
        val guildMock: Guild = mockk()
        every { guildMock.name } returns serverName
        every { jda.getGuildById(serverId) } returns guildMock
        every { memeRepository.getMemes(serverId) } returns emptyList()
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(view().name("fragments/memeList.html"))
            .andExpect(status().isOk)
            .andExpect(model().attribute("serverName", serverName))
    }

    @Test
    fun `meme list page contains 'the server' as server name when Discord client is not reachable`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns emptyList()
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(view().name("fragments/memeList.html"))
            .andExpect(status().isOk)
            .andExpect(model().attribute("serverName", "the server"))
    }

    @ParameterizedTest
    @MethodSource("provideVideoTypesArgument")
    fun `meme list should contain video type meme when url has supported file extension`(memeUrl: String) {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns listOf(Meme("name", memeUrl, 2137))
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(hasProperty<MediaType>("type", equalTo(MemeMediaType.VIDEO)))
                )
            )
    }

    @ParameterizedTest
    @MethodSource("provideImageTypesArgument")
    fun `meme list should contain image type meme when url has supported file extension`(memeUrl: String) {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns listOf(Meme("name", memeUrl, 2137))
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(hasProperty<MediaType>("type", equalTo(MemeMediaType.IMAGE)))
                )
            )
    }

    @Test
    fun `meme list should contain unknown type meme when url has no file extension`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns listOf(Meme("name", "www.example.com/meme", 2137))
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(hasProperty<MediaType>("type", equalTo(MemeMediaType.UNKNOWN)))
                )
            )
    }

    @Test
    fun `meme list should have 'dot gif' at the end of the url if it is form tenor`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns listOf(Meme("name", "www.tenor.com/meme", 2137))
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(hasProperty<String>("url", equalTo("www.tenor.com/meme.gif")))
                )
            )
    }

    @Test
    fun `meme list should be sorted by name if sortByCount parameter is not provided`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns generateMemeList()
        //when & then
        mockMvc.perform(get("/memes/$serverId"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(
                        hasProperty<String>("name", equalTo("aaa")),
                        hasProperty("name", equalTo("bbb")),
                        hasProperty("name", equalTo("ccc"))
                    )
                )
            )
    }

    @Test
    fun `meme list should be sorted by name if sortByCount parameter is false`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns generateMemeList()
        //when & then
        mockMvc.perform(get("/memes/$serverId").queryParam("sortByCount", "false"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(
                        hasProperty<String>("name", equalTo("aaa")),
                        hasProperty("name", equalTo("bbb")),
                        hasProperty("name", equalTo("ccc"))
                    )
                )
            )
    }

    @Test
    fun `meme list should be sorted by count if sortByCount parameter is true`() {
        //given
        val serverId = 2137L
        every { jda.getGuildById(serverId) } returns null
        every { memeRepository.getMemes(serverId) } returns generateMemeList()
        //when & then
        mockMvc.perform(get("/memes/$serverId").queryParam("sortByCount", "true"))
            .andExpect(
                model().attribute(
                    "memes",
                    contains(
                        hasProperty<String>("name", equalTo("ccc")),
                        hasProperty("name", equalTo("bbb")),
                        hasProperty("name", equalTo("aaa"))
                    )
                )
            )
    }

    private fun generateMemeList() = listOf(
        Meme("bbb", "example.com", 2),
        Meme("aaa", "example.com", 1),
        Meme("ccc", "example.com", 3)
    )

    companion object {
        private const val baseUrl = "www.example.com/meme."

        @JvmStatic
        fun provideVideoTypesArgument(): Stream<Arguments> = Stream.of(
            Arguments.of(baseUrl + "mp4"),
            Arguments.of(baseUrl + "mov"),
            Arguments.of(baseUrl + "webm"),
            Arguments.of(baseUrl + "mov")
        )

        @JvmStatic
        fun provideImageTypesArgument(): Stream<Arguments> = Stream.of(
            Arguments.of(baseUrl + "jpg"),
            Arguments.of(baseUrl + "JPG"),
            Arguments.of(baseUrl + "jpeg"),
            Arguments.of(baseUrl + "png"),
            Arguments.of(baseUrl + "PNG"),
            Arguments.of(baseUrl + "gif"),
            Arguments.of(baseUrl + "gifv")
        )
    }
}