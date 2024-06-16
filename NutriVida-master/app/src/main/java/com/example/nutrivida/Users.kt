package com.example.nutrivida

//Clase Usuario donde se almacenan toda la informacion de los usuarios
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