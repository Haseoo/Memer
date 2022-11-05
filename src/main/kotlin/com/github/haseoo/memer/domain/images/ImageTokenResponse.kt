package com.github.haseoo.memer.domain.images

import com.fasterxml.jackson.annotation.JsonCreator

data class ImageTokenResponse @JsonCreator constructor(val token: String)