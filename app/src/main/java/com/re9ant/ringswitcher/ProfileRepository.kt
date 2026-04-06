package com.re9ant.ringswitcher

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProfileRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("profiles_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveProfiles(profiles: List<AudioProfile>) {
        val json = gson.toJson(profiles)
        prefs.edit().putString("profiles", json).apply()
    }

    fun getProfiles(): List<AudioProfile> {
        val json = prefs.getString("profiles", null) ?: return emptyList()
        val type = object : TypeToken<List<AudioProfile>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveActiveProfileId(id: String?) {
        if (id == null) {
            prefs.edit().remove("active_profile_id").apply()
        } else {
            prefs.edit().putString("active_profile_id", id).apply()
        }
    }

    fun getActiveProfileId(): String? {
        return prefs.getString("active_profile_id", null)
    }
}
