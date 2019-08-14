package com.colorhaake.elmkotlin.sample

import com.colorhaake.elmkotlin.Msg

interface AppMsg: Msg

class MsgNone : AppMsg {
    override fun type(): String = type
    companion object {
        const val type = "MsgNone"
    }
}

data class MsgUpdateAppCounter(val number: Int) : AppMsg {
    override fun type(): String = type
    companion object {
        const val type = "MsgUpdateAppCounter"
    }
}
