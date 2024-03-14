package com.example.bottom_navigation_view.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.bottom_navigation_view.MainActivity
import com.example.bottom_navigation_view.R


class DashboardFragment : Fragment() {
    private lateinit var view : View
    private lateinit var checkPointView: TextView
    private lateinit var walkCountView: TextView
    private lateinit var visitCountView: TextView
    private lateinit var area_EditText: EditText
    private lateinit var area_SeekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        checkPointView = view.findViewById(R.id.number_of_checkpoints)
        walkCountView = view.findViewById(R.id.number_of_steps)
        visitCountView = view.findViewById(R.id.number_of_visits)

        val childFragment = BadgeListFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, childFragment)
        transaction.commit()

        viewUpdate()
        // 先ほどのレイアウトをここでViewとして作成します
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        area_EditText = view.findViewById(R.id.area_EditText)
        area_SeekBar = view.findViewById(R.id.area_SeekBar)

        area_EditText.setText("15")
        area_SeekBar.progress = 15


        area_EditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) //Enterキー押下時
            {
                try {
                    //入力された値が空または255を超えていないか判定する
                    if (area_EditText.text.toString() == "") {
                        area_EditText.setText("1")
                    } else if (Integer.valueOf(area_EditText.text.toString()) > 255) {
                        area_EditText.setText("20")
                    }
                } catch (e: NumberFormatException) {
                    area_EditText.setText("20")
                }

                //値を反映
                val b = Integer.valueOf(area_EditText.text.toString())
                area_SeekBar.progress = b //入力された値をシークバーに反映させる
            }
            false
        }

        //シークバータッチ時
        area_SeekBar.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int, fromUser: Boolean
                ) {
                    area_EditText.setText(area_SeekBar.progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val mainActivity = activity as MainActivity
                    val checkPointFlagCheck = mainActivity.checkPointFlagCheck
                    checkPointFlagCheck.AREA = area_SeekBar.progress
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewUpdate()
    }

    fun viewUpdate(){
        val listSize = BadgeFlag.checkPointFlag.size
        val trueCount = BadgeFlag.checkPointFlag.count { it == true }
        checkPointView.text = "$trueCount/$listSize"

        val mainActivity = activity as MainActivity
        val variableValue = mainActivity.steps
        walkCountView.text = "$variableValue"

        val visitCount = mainActivity.visitCount
        visitCountView.text = "${visitCount.getVisitCount()}"
    }
}