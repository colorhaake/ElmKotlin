package com.colorhaake.elmkotlin.sample

import com.colorhaake.elmkotlin.*

val updateAppCounter: Update<MsgUpdateAppCounter, AppModel> = { msg -> {
    model -> Reaction(model.copy(count = model.count + msg.number), none())
} }

val updateMap: Map<String, Update<AppMsg, AppModel>> = mapOf(
        MsgUpdateAppCounter.type to createUpdate(updateAppCounter)
)

