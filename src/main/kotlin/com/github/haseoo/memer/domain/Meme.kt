package com.github.haseoo.memer.domain

data class Meme(val name: String,
           val url: String,
           val count: Int) {

    constructor(record: Map<String, String>) : this(record[NAME_MEME_RECORD_FIELD_NAME]!!,
        record[URL_MEME_RECORD_FIELD_NAME]!!,
        record[COUNT_MEME_RECORD_FIELD_NAME]!!.toInt()
    )

    fun toMap() = mapOf(
        NAME_MEME_RECORD_FIELD_NAME to this.name,
        URL_MEME_RECORD_FIELD_NAME to this.url,
        COUNT_MEME_RECORD_FIELD_NAME to this.count.toString()
    )
}

const val NAME_MEME_RECORD_FIELD_NAME = "name"
const val URL_MEME_RECORD_FIELD_NAME = "url"
const val COUNT_MEME_RECORD_FIELD_NAME = "count"