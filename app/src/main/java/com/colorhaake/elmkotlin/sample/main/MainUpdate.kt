package com.colorhaake.elmkotlin.sample.main

import com.colorhaake.elmkotlin.Reaction
import com.colorhaake.elmkotlin.Update
import com.colorhaake.elmkotlin.createUpdate
import com.colorhaake.elmkotlin.sample.counter.CounterActivity
import io.reactivex.Observable

val clickCounterExample: Update<MsgClickCounterExample, MainModel> = { msg -> { model ->
    Reaction(model, Observable.fromCallable { CounterActivity.launch(msg.context) })
}}

val updateMap: Map<String, Update<MainMsg, MainModel>> = mapOf(
        MsgClickCounterExample.type to createUpdate(clickCounterExample)
)

