package com.github.haseoo.memer.controller.view

import com.github.haseoo.memer.domain.Meme

class MemeView(meme: Meme) {
    val name: String
    val url: String
    val count: Long
    val type: MemeMediaType

    init {
        name = meme.name
        url = if(meme.url.contains("tenor.com")) meme.url + ".gif" else meme.url
        count = meme.count.toLong()
        type = getMediaType(meme.url)
    }


}