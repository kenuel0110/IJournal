package com.twostrangerteam.ijournal.classes

class User {
    var uid: String? = null
    var nick: String? = null
    var profileImageUrl: String? = null
    var crypt: String? = null
    var email: String? = null

    constructor(uid:String, nick: String, profileImageUrl: String, crypt: String, email: String){
        this.uid = uid
        this.nick = nick
        this.profileImageUrl = profileImageUrl
        this.crypt = crypt
        this.email = email
    }
}