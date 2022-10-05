package com.twostrangerteam.ijournal.classes


class CabinetModel (
    var num_cab: String = "",
    var lastname: String = "",

){
    constructor(): this("","",)
}

//Класс домашки с конструктором пользователя
class Cabinet(
    var num_cab: String = "",
    var lastname: String = ""
)