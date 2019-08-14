package com.colorhaake.elmkotlin.sample.main

import android.content.Context
import com.colorhaake.elmkotlin.Msg

interface MainMsg : Msg

class MsgClickCounterExample(val context: Context) : MainMsg {
    override fun type(): String = type
    companion object {
        const val type = "MainMsgClickCounterExample"
    }
}
