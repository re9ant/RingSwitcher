package com.re9ant.ringswitcher

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.re9ant.ringswitcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = ProfileAdapter(
            onProfileClick = { profile ->
                if (checkDndPermission()) {
                    viewModel.applyProfile(profile)
                    Toast.makeText(this, "Profile applied: ${profile.name}", Toast.LENGTH_SHORT).show()
                }
            },
            onProfileLongClick = { profile ->
                showEditDeleteDialog(profile)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        viewModel.profiles.observe(this) { profiles ->
            adapter.submitList(profiles)
        }

        viewModel.activeProfileId.observe(this) { activeId ->
            adapter.activeProfileId = activeId
        }

        checkDndPermission()
    }

    override fun onResume() {
        super.onResume()
        // Re-check permissions when returning and refresh data
        viewModel.loadData()
        adapter.notifyDataSetChanged()
    }

    private fun checkDndPermission(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission Required")
                .setMessage("RingSwitcher needs Do Not Disturb access to manage system volumes properly. Please grant it in settings.")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
            return false
        }
        return true
    }

    private fun showEditDeleteDialog(profile: AudioProfile) {
        val options = arrayOf("Edit", "Delete")
        MaterialAlertDialogBuilder(this)
            .setTitle(profile.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, ProfileEditActivity::class.java)
                        intent.putExtra("profile_id", profile.id)
                        startActivity(intent)
                    }
                    1 -> {
                        viewModel.deleteProfile(profile.id)
                    }
                }
            }
            .show()
    }
}
