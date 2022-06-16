package com.ma.lightweather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnKotlin()
    }

    private fun learnKotlin() {
        var a=10
        a *=10
        val b=add(10,11)
        val c=getScore("h")
        Log.e("abc", "a=$a===b=$b===scor=$c")
        circle()
        val person=Person()
        person.name="haha"
        person.age=10
        person.eat()
    }

    private fun add(a: Int, b: Int)=if (a > b) a else b

    private fun getScore(name: String)=when(name){
        "h"->90
        "j"->80
        "k"->70
        else -> 0
    }

    private fun circle(){
        val r=0..10
        val u=0 until 10
        val d=10 downTo 0 step 2
        for (i in d){
            Log.e("abc", "i=$i")
        }
    }
}