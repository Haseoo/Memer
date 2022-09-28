package com.github.haseoo.memer.controller.view


enum class MemeMediaType {
    IMAGE, VIDEO, UNKNOWN
}

fun getMediaType(url: String): MemeMediaType {
    if (url.contains("/mp4"))
        return MemeMediaType.VIDEO
    val extensionPosition = url.lastIndexOf(".")
    if(extensionPosition == -1) {
        return MemeMediaType.UNKNOWN
    }
    val fileExtension = url.substring(extensionPosition)
    return if(fileExtension in videosTypes)
        MemeMediaType.VIDEO
    else
        MemeMediaType.IMAGE
}

private val videosTypes = arrayOf(".mp4", ".mov", ".webm", ".mov")