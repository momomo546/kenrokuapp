package com.example.kenroku_app.view.fragments.achieve.badge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kenroku_app.R
import com.example.kenroku_app.model.repositories.data.AchieveData

//BadgeDataには(R.drawable.xxx,R.string.xxx)のようにバッジ獲得後に表示する画像とテキストのIDを入れる
data class BadgeData(val imageResId: Int, val textId: Int)

class BadgeAdapter (private val data: List<BadgeData>) : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //fragment_badge.xmlのレイアウトを取得
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_badge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //Badgeの取得状況をbool値で保存してあり、これを利用
        if (AchieveData.seasonFlag[position]) {
            //trueのときはバッジの画像を表示
            holder.imageView.setImageResource(item.imageResId)
        } else {
            //falseのときははてなの画像を表示
            holder.imageView.setImageResource(R.drawable.badge_default)
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