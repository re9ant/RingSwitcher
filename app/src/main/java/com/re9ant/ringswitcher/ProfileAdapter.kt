package com.re9ant.ringswitcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(
    private val onProfileClick: (AudioProfile) -> Unit,
    private val onProfileLongClick: (AudioProfile) -> Unit
) : ListAdapter<AudioProfile, ProfileAdapter.ProfileViewHolder>(ProfileDiffCallback()) {

    var activeProfileId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        private val ivActiveIndicator: ImageView = itemView.findViewById(R.id.ivActiveIndicator)
        private val container: LinearLayout = itemView.findViewById(R.id.container)

        fun bind(profile: AudioProfile) {
            tvName.text = profile.name
            tvDetails.text = "Vol: ${profile.ringVolume} | DND: ${if(profile.isDndEnabled) "On" else "Off"}"
            
            if (profile.id == activeProfileId) {
                ivActiveIndicator.visibility = View.VISIBLE
                container.setBackgroundColor(0x1A000000)
            } else {
                ivActiveIndicator.visibility = View.GONE
                container.setBackgroundColor(0x00000000)
            }

            itemView.setOnClickListener { onProfileClick(profile) }
            itemView.setOnLongClickListener {
                onProfileLongClick(profile)
                true
            }
        }
    }

    class ProfileDiffCallback : DiffUtil.ItemCallback<AudioProfile>() {
        override fun areItemsTheSame(oldItem: AudioProfile, newItem: AudioProfile) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: AudioProfile, newItem: AudioProfile) = oldItem == newItem
    }
}
