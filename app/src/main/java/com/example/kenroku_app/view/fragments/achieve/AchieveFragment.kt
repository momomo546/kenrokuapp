package com.example.kenroku_app.view.fragments.achieve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.kenroku_app.R
import com.example.kenroku_app.databinding.FragmentAchieveBinding
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeListFragment
import com.example.kenroku_app.viewmodel.AchieveViewModel

class AchieveFragment : Fragment() {

    private var _binding: FragmentAchieveBinding? = null
    private lateinit var achieveViewModel: AchieveViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        achieveViewModel = ViewModelProvider(this)[AchieveViewModel::class.java]

        _binding = FragmentAchieveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val walkCountView: TextView = binding.numberOfSteps
        val checkPointView: TextView = binding.numberOfCheckpoints
        val visitCountView: TextView = binding.numberOfVisits
        achieveViewModel.checkPointText.observe(viewLifecycleOwner) {
            checkPointView.text = it
        }
        achieveViewModel.walkCountText.observe(viewLifecycleOwner) {
            walkCountView.text = it
        }
        achieveViewModel.visitCountText.observe(viewLifecycleOwner){
            visitCountView.text = it
        }

        val childFragment = BadgeListFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, childFragment)
        transaction.commit()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        achieveViewModel.viewUpdate()
    }

    /*fun viewUpdate(){
        val listSize = MarkerData.checkPointFlag.size
        val trueCount = MarkerData.checkPointFlag.count { it }
        checkPointView.text = "$trueCount/$listSize"

        val mainActivity = activity as MainActivity
        val variableValue = MarkerData.steps
        walkCountView.text = "$variableValue"

        val visitCount = mainActivity.visitCount
        visitCountView.text = "${visitCount.getVisitCount()}"
    }*/
}