package com.example.nutrivida

class Users (
    var email:String,
    var password:String,
    var name:String,
    var gender:String,
    var age:Int,
    var physicalAct:Int,
    var heigth:Float,
    var weigth:Float,
    var goal:String) {

    companion object{
        var user = mutableListOf<Users>()
    }
}