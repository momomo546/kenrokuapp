package com.example.kenroku_app.model.services.gps.actions

import android.content.Context
import com.example.kenroku_app.model.repositories.data.AchieveData
import java.util.Calendar


class VisitCount(private val context: Context){
    // 変数保存
    private val sharedPreferences = context.getSharedPreferences("VisitCount", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private var count = sharedPreferences.getInt("count", 0)
    private var year = sharedPreferences.getInt("year", 2000)
    private var month = sharedPreferences.getInt("month", 1)
    private var date = sharedPreferences.getInt("date", 1)

    init{
        AchieveData.visitCount = count
    }

    fun add(){
        if (isVisitCount()) {
            count++
            editor.putInt("count", count)
            editor.apply()
            AchieveData.visitCount = count
        }
    }

    fun getVisitCount(): Int {
        return count
    }

    private fun isVisitCount(): Boolean {
        val calCheck: Calendar = Calendar.getInstance()
        if (calCheck[Calendar.YEAR]>year) return calenderUpdate(calCheck)
        if (calCheck[Calendar.MONTH]+1>month) return calenderUpdate(calCheck)
        if (calCheck[Calendar.DATE]>date) return calenderUpdate(calCheck)
        return false
    }

    private fun calenderUpdate(calender: Calendar):Boolean{
        year = calender[Calendar.YEAR]
        month = calender[Calendar.MONTH] + 1
        date = calender[Calendar.DATE]

        editor.putInt("year", year)
        editor.putInt("month", month)
        editor.putInt("date", date)
        editor.apply()

        return true
    }
}