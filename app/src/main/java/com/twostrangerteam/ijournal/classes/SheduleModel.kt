package com.twostrangerteam.ijournal.classes

class SheduleModel(
    var dayOfWeek: String = "",
    var name: String = "",
    var teacher: String = "",
    var type: String = ""
    ){
        constructor(): this("","","","")
    }

    //Класс расписания с конструктором
    class Shedule(
        var dayOfWeek: String = "",
        var name: String = "",
        var teacher: String = "",
        var type: String = "",
    )