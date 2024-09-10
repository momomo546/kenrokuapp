package com.example.kenroku_app.model.repositories.data

class AchieveData {
    companion object {
        var checkPointFlag = MutableList(27) { false }
        var seasonFlag = MutableList(4){ false }
        var steps = 0
        var visitCount=0
    }
}
