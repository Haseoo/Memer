package com.github.haseoo.memer.service

import com.github.haseoo.memer.config.Env
import com.github.haseoo.memer.domain.exception.LinkExpiredException
import com.github.haseoo.memer.domain.images.ImageTokenResponse
import com.github.haseoo.memer.domain.images.ImagesAuthData
import com.github.haseoo.memer.repository.ImagesAuthDataRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.security.SecureRandom
import java.util.*


@Service
class ImageService(
    private val _restTemplate: RestTemplate,
    private val _secureRandom: SecureRandom,
    private val _imagesAuthDataRepository: ImagesAuthDataRepository,
    private val _env: Env
) {

    fun generateImageLink(
        serverId: Long,
        serverName: String,
        channelIds: Collection<Long>
    ): String {
        val idBytes = ByteArray(64)
        _secureRandom.nextBytes(idBytes)
        val id = Base64.getUrlEncoder().encodeToString(idBytes)
        _imagesAuthDataRepository.saveAuthData(id, ImagesAuthData(serverId, serverName, channelIds))
        return "${_env.memerUrl}/images?id=$id"
    }

    fun generateImageToken(authDataId: String): String {
        val authDataJson = _imagesAuthDataRepository.popAuthData(authDataId)
            ?: throw LinkExpiredException()
        val result: ImageTokenResponse?
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val entity = HttpEntity<String>(authDataJson, headers)
            result = _restTemplate.postForObject(
                "${_env.dscViewerUrl}/api/token",
                entity,
                ImageTokenResponse::class.java
            )
        } catch (e: Exception) {
            throw IllegalStateException("Image server is down or unexpected exception occurred.", e)
        }
        return result?.token ?: throw IllegalStateException("Token not available, image server might be down!")
    }
}