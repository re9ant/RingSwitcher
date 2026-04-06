package com.re9ant.ringswitcher

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build

class AudioProfileManager(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun isDndPermissionGranted(): Boolean {
        return notificationManager.isNotificationPolicyAccessGranted
    }

    fun applyProfile(profile: AudioProfile) {
        // Apply Do Not Disturb first
        if (isDndPermissionGranted()) {
            if (profile.isDndEnabled) {
                // Set DND: allow alarms only for instance
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
            } else {
                // Turn off DND
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
        }

        // We must ensure the ringer mode is NORMAL before attempting to set ring volume > 0,
        // otherwise Android might ignore the volume change.
        if (!profile.isDndEnabled) {
             if (profile.ringVolume == 0) {
                 if (profile.isVibrateEnabled) {
                     audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                 } else {
                     audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                 }
             } else {
                 audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
             }
        }

        // Calculate volumes relative to stream max volumes
        setVolume(AudioManager.STREAM_RING, profile.ringVolume)
        setVolume(AudioManager.STREAM_NOTIFICATION, profile.notificationVolume)
        setVolume(AudioManager.STREAM_MUSIC, profile.mediaVolume)
    }

    private fun setVolume(streamType: Int, percentage: Int) {
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val safePercentage = percentage.coerceIn(0, 100)
        val targetVolume = (maxVolume * (safePercentage / 100f)).toInt()
        
        try {
            audioManager.setStreamVolume(streamType, targetVolume, 0)
        } catch (e: SecurityException) {
            // Might happen if system restricts volume changes dynamically
            e.printStackTrace()
        }
    }
}
