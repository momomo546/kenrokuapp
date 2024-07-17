package com.example.kenroku_app.view.fragments.achieve.badge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.kenroku_app.R

class BadgeFragment : Fragment() {

    companion object {
        private const val ARG_IMAGE_RESOURCE = "img_resource"
        private const val ARG_TEXT = "text"

        fun newInstance(imageResource: Int, text: String): BadgeFragment {
            val fragment = BadgeFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE_RESOURCE, imageResource)
            args.putString(ARG_TEXT, text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_badge, container, false)
        val imageView = view.findViewById<ImageView>(R.id.img_badge)
        val textView = view.findViewById<TextView>(R.id.text_badge)

        arguments?.let {
            imageView.setImageResource(it.getInt(ARG_IMAGE_RESOURCE))
            textView.text = it.getString(ARG_TEXT)
        }

        return view
    }
}