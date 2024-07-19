package com.example.kenroku_app.view.fragments.achieve.badge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kenroku_app.R
import com.example.kenroku_app.model.repositories.data.MarkerData

data class BadgeData(val imageResId: Int, val textId: Int)

class BadgeAdapter (private val data: List<BadgeData>) : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_badge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        if (!MarkerData.seasonFlag[position]) {
            holder.imageView.setImageResource(R.drawable.badge_default)
        } else {
            holder.imageView.setImageResource(item.imageResId)
        }
        holder.textView.setText(item.textId)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_badge)
        val textView: TextView = itemView.findViewById(R.id.text_badge)
    }
}