package com.re9ant.ringswitcher

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.re9ant.ringswitcher.databinding.ActivityProfileEditBinding
import java.util.UUID

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var editingProfileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editingProfileId = intent.getStringExtra("profile_id")
        
        binding.toolbar.setNavigationOnClickListener { finish() }

        if (editingProfileId != null) {
            binding.toolbar.title = "Edit Profile"
            loadExistingProfile()
        } else {
            binding.toolbar.title = "New Profile"
            binding.sbRingVolume.progress = 50
            binding.sbNotificationVolume.progress = 50
            binding.sbMediaVolume.progress = 50
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadExistingProfile() {
        viewModel.profiles.observe(this) { profiles ->
            val profile = profiles.find { it.id == editingProfileId }
            if (profile != null) {
                binding.etName.setText(profile.name)
                binding.sbRingVolume.progress = profile.ringVolume
                binding.sbNotificationVolume.progress = profile.notificationVolume
                binding.sbMediaVolume.progress = profile.mediaVolume
                binding.swVibrate.isChecked = profile.isVibrateEnabled
                binding.swDnd.isChecked = profile.isDndEnabled
                
                viewModel.profiles.removeObservers(this)
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etName.error = "Name cannot be empty"
            return
        }

        val profile = AudioProfile(
            id = editingProfileId ?: UUID.randomUUID().toString(),
            name = name,
            ringVolume = binding.sbRingVolume.progress,
            notificationVolume = binding.sbNotificationVolume.progress,
            mediaVolume = binding.sbMediaVolume.progress,
            isVibrateEnabled = binding.swVibrate.isChecked,
            isDndEnabled = binding.swDnd.isChecked
        )

        viewModel.saveProfile(profile)
        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}
