package com.example.kenroku_app.view.fragments.achieve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.kenroku_app.R
import com.example.kenroku_app.view.activities.MainActivity
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeListFragment
import com.example.kenroku_app.model.repositories.data.MarkerData


class AchieveFragment : Fragment() {
    private lateinit var view : View
    private lateinit var checkPointView: TextView
    private lateinit var walkCountView: TextView
    private lateinit var visitCountView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        view = inflater.inflate(R.layout.fragment_achieve, container, false)
        checkPointView = view.findViewById(R.id.number_of_checkpoints)
        walkCountView = view.findViewById(R.id.number_of_steps)
        visitCountView = view.findViewById(R.id.number_of_visits)

        val childFragment = BadgeListFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, childFragment)
        transaction.commit()

        viewUpdate()

        return view
    }

    override fun onResume() {
        super.onResume()
        viewUpdate()
    }

    fun viewUpdate(){
        val listSize = MarkerData.checkPointFlag.size
        val trueCount = MarkerData.checkPointFlag.count { it == true }
        checkPointView.text = "$trueCount/$listSize"

        val mainActivity = activity as MainActivity
        val variableValue = MarkerData.steps
        walkCountView.text = "$variableValue"

        val visitCount = mainActivity.visitCount
        visitCountView.text = "${visitCount.getVisitCount()}"
    }
}