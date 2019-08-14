package com.colorhaake.elmkotlin.sample.counter

import com.colorhaake.elmkotlin.Msg

interface CounterMsg : Msg

data class MsgUpdateCounter(val number: Int) : CounterMsg {
    override fun type(): String = type
    companion object {
        const val type = "CounterMsgUpdateCounter"
    }
}

class MsgClickAddButton : CounterMsg {
    override fun type(): String = type
    companion object {
        const val type = "CounterMsgClickAddButton"
    }
}

class MsgClickSubButton : CounterMsg {
    override fun type(): String = type
    companion object {
        const val type = "CounterMsgClickSubButton"
    }
}
