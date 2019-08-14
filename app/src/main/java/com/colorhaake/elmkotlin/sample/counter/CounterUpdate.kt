package com.colorhaake.elmkotlin.sample.counter

import com.colorhaake.elmkotlin.*
import com.colorhaake.elmkotlin.sample.MsgUpdateAppCounter
import io.reactivex.Observable

val updateCounter: Update<MsgUpdateCounter, CounterModel> = { msg -> {
    model -> Reaction(model.copy(count = msg.number), none())
} }

val clickAddButton: Update<MsgClickAddButton, CounterModel> = { _ -> {
    model -> Reaction(model, Observable.just(MsgUpdateAppCounter(1)))
} }

val clickSubButton: Update<MsgClickSubButton, CounterModel> = { _ -> {
    model -> Reaction(model, Observable.just(MsgUpdateAppCounter(-1)))
} }

val updateMap: Map<String, Update<CounterMsg, CounterModel>> = mapOf(
        MsgUpdateCounter.type to createUpdate(updateCounter),
        MsgClickAddButton.type to createUpdate(clickAddButton),
        MsgClickSubButton.type to createUpdate(clickSubButton)
)
