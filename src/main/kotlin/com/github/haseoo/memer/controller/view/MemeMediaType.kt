package com.github.haseoo.memer.controller.view


enum class MemeMediaType {
    IMAGE, VIDEO, UNKNOWN
}

fun getMediaType(url: String): MemeMediaType {
    if (url.contains("/mp4"))
        return MemeMediaType.VIDEO
    val extensionPosition = url.lastIndexOf(".")
    if (extensionPosition == -1) {
        return MemeMediaType.UNKNOWN
    }
    return when (url.substring(extensionPosition)) {
        in videoTypes -> MemeMediaType.VIDEO
        in imageTypes -> MemeMediaType.IMAGE
        else -> MemeMediaType.UNKNOWN
    }
}

private val videoTypes = arrayOf(".mp4", ".mov", ".webm", ".mov")
private val imageTypes = arrayOf(".jpg", ".jpeg", ".JPG", ".gif", ".gifv", ".png", ".PNG")