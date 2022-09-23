package com.github.haseoo.memer.job

import com.github.haseoo.memer.domain.Meme
import com.github.haseoo.memer.repository.MemeRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL


@Service
class MemeCleanupCron(private val memeRepository: MemeRepository) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(cron = "0 0 0 ? * WED")
    fun cleanupMemes() {
        logger.info { "Starting meme cleanup" }
        memeRepository.removeIf { isMemeNotAvailable(it) }
        logger.info { "Meme cleanup finished" }
    }

    private fun isMemeNotAvailable(meme: Meme): Boolean {
        return try {
            val url = URL(meme.url)
            val huc = url.openConnection() as HttpURLConnection
            huc.requestMethod = "HEAD"
            huc.responseCode != HttpURLConnection.HTTP_OK
        } catch (ignored: Exception) {
            true
        }
    }
}