package com.example.kenroku_app.view.fragments.achieve.badge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kenroku_app.databinding.FragmentBadgeBinding
import com.example.kenroku_app.viewmodel.Badge.BadgeViewModel

class BadgeFragment : Fragment() {

    private var _binding: FragmentBadgeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val badgeViewModel =
            ViewModelProvider(this)[BadgeViewModel::class.java]

        _binding = FragmentBadgeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageView: ImageView = binding.imageBadge
        val textView: TextView = binding.textBadge
        badgeViewModel.imageBadge.observe(viewLifecycleOwner) { imageResource ->
            imageView.setImageResource(imageResource)
        }
        badgeViewModel.textBadge.observe(viewLifecycleOwner) { text ->
            textView.setText(text)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}