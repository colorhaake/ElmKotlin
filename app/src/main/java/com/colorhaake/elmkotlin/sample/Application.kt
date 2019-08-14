package com.colorhaake.elmkotlin.sample

import android.view.View
import com.colorhaake.elmkotlin.*
import com.colorhaake.elmkotlin.sample.base.BaseApplication
import com.colorhaake.elmkotlin.sample.base.RxApplicationLifecycle

class Application : BaseApplication<AppMsg, AppModel, View>() {

    override fun onCreateProgram(lifecycle: RxApplicationLifecycle): Component<AppMsg, AppModel, View> {
        val init = Reaction(initAppModel(), none())
        val view = { _: AppModel -> View(this) }
        val update = { msg: AppMsg -> updateMap.getValue(msg.type())(msg) }
        val subscriptions = { _: AppModel -> none() }

        return createProgram(init, view, update, subscriptions)
    }
}
