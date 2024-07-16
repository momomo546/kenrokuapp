package com.example.kenroku_app.fragments

import android.content.Context
import java.util.Calendar


class VisitCount(private val context: Context){
    // 変数保存
    val sharedPreferences = context.getSharedPreferences("VisitCount", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    var count = sharedPreferences.getInt("count", 0)
    var year = sharedPreferences.getInt("year", 2000)
    var month = sharedPreferences.getInt("month", 1)
    var date = sharedPreferences.getInt("date", 1)


    fun add(){
        if (isVisitCount()) count++
        editor.putInt("count", count)
        editor.apply()
    }

    fun getVisitCount(): Int {
        return count
    }

    fun isVisitCount(): Boolean {
        val calCheck: Calendar = Calendar.getInstance()
        if (calCheck[Calendar.YEAR]>year) return calenderUpdate(calCheck)
        if (calCheck[Calendar.MONTH]+1>month) return calenderUpdate(calCheck)
        if (calCheck[Calendar.DATE]>date) return calenderUpdate(calCheck)
        return false
    }

    fun calenderUpdate(calender: Calendar):Boolean{
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