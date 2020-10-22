package com.sihaloho.travelku.modul

class Chatlist {
    private var id: String = ""

    constructor()
    constructor(id: String) {
        this.id = id
    }


    fun getId(): String?{
        return id
    }
    fun setgetId(id: String?){
        this.id = id!!
    }

}