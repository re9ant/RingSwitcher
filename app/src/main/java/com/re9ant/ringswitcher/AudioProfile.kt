package com.re9ant.ringswitcher

import java.util.UUID

data class AudioProfile(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var ringVolume: Int,
    var notificationVolume: Int,
    var mediaVolume: Int,
    var isVibrateEnabled: Boolean,
    var isDndEnabled: Boolean
)
