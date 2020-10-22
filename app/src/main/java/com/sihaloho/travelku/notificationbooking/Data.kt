package com.sihaloho.travelku.notificationbooking

class Data {

    private var user: String= ""
    private var icon = 0
    private var body: String= ""
    private var title: String= ""
    private var sented: String= ""

    constructor(){}

    constructor(user: String, icon: Int, body: String, title: String, sented: String) {
        this.user = user
        this.icon = icon
        this.body = body
        this.title = title
        this.sented = sented
    }

    fun getUser():String?{
        return user
    }
    fun setUser(user: String){
        this.user = user
    }

 fun getIcon():Int{
        return icon
    }
    fun setIcon(icon: Int){
        this.icon = icon
    }

 fun getBody():String?{
        return body
    }
    fun setBody(body: String){
        this.body = body
    }

 fun getTitle():String?{
        return title
    }
    fun setTitle(tittle: String){
        this.title = tittle
    }

 fun getSented():String?{
        return sented
    }
    fun setSented(sented: String){
        this.sented = sented
    }


}