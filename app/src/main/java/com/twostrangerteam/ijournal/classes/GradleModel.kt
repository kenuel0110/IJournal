package com.twostrangerteam.ijournal.classes

class GradleModel (
    var uuid_gr: String = "",
    var uuid_user: String = "",
    var gradle_title: String = "",
    var gradle_lesson: String = "",
    var gradle_date: String = ""

    ){
    constructor(): this("","","", "")
}

//Класс домашки с конструктором пользователя
class Gradle(
    var uuid_gr: String = "",
    var uuid_user: String = "",
    val gradle_title: String = "",
    val gradle_lesson: String = "",
    val gradle_date: String = ""
)