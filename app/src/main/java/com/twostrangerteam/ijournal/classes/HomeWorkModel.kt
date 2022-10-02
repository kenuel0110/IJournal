package com.twostrangerteam.ijournal.classes

class HomeWorkModel (
    var uuid_hw: String = "",
    var title: String = "",
    var lesson: String = "",
    var quest: String = "",
    var data_start: String = "",
    var data_end: String = ""
    ){
        constructor(): this("","","", "","","")
    }

//Класс домашки с конструктором пользователя
class HomeWork(
    var uuid_hw: String = "",
    var title: String = "",
    var lesson: String = "",
    var quest: String = "",
    var data_start: String = "",
    var data_end: String = ""
)