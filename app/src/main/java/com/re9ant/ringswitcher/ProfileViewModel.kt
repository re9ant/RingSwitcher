package com.re9ant.ringswitcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProfileRepository(application)
    private val audioManager = AudioProfileManager(application)

    private val _profiles = MutableLiveData<List<AudioProfile>>()
    val profiles: LiveData<List<AudioProfile>> get() = _profiles

    private val _activeProfileId = MutableLiveData<String?>()
    val activeProfileId: LiveData<String?> get() = _activeProfileId

    init {
        loadData()
    }

    fun loadData() {
        val loadedProfiles = repository.getProfiles()
        if (loadedProfiles.isEmpty()) {
            val defaultProfile = AudioProfile(
                name = "Normal",
                ringVolume = 80,
                notificationVolume = 80,
                mediaVolume = 50,
                isVibrateEnabled = true,
                isDndEnabled = false
            )
            val silentProfile = AudioProfile(
                name = "Silent",
                ringVolume = 0,
                notificationVolume = 0,
                mediaVolume = 0,
                isVibrateEnabled = false,
                isDndEnabled = true
            )
            val initialProfiles = listOf(defaultProfile, silentProfile)
            repository.saveProfiles(initialProfiles)
            _profiles.value = initialProfiles
        } else {
            _profiles.value = loadedProfiles
        }
        _activeProfileId.value = repository.getActiveProfileId()
    }

    fun applyProfile(profile: AudioProfile) {
        audioManager.applyProfile(profile)
        repository.saveActiveProfileId(profile.id)
        _activeProfileId.value = profile.id
    }

    fun saveProfile(profile: AudioProfile) {
        val currentList = _profiles.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == profile.id }
        if (index != -1) {
            currentList[index] = profile
        } else {
            currentList.add(profile)
        }
        repository.saveProfiles(currentList)
        _profiles.value = currentList
        
        if (profile.id == _activeProfileId.value) {
            applyProfile(profile)
        }
    }

    fun deleteProfile(profileId: String) {
        val currentList = _profiles.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == profileId }
        repository.saveProfiles(currentList)
        _profiles.value = currentList
        
        if (_activeProfileId.value == profileId) {
            repository.saveActiveProfileId(null)
            _activeProfileId.value = null
        }
    }
}
