package com.example.kenroku_app.fragments.achieve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kenroku_app.R

class BadgeListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_badge_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val badgeList = listOf(
            MyData(R.drawable.img_marker_04, getString(R.string.spring_badge)),
            MyData(R.drawable.img_marker_06, getString(R.string.summer_badge)),
            MyData(R.drawable.img_marker_10, getString(R.string.fall_badge)),
            MyData(R.drawable.img_marker_12, getString(R.string.winter_badge)),
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        val adapter = BadgeAdapter(badgeList)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        return view
    }
}