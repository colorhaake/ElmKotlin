package com.colorhaake.elmkotlin.sample.main

import android.content.Context
import android.view.View
import com.colorhaake.elmkotlin.*
import com.colorhaake.elmkotlin.sample.AppModel
import com.colorhaake.elmkotlin.sample.base.BaseActivity
import com.colorhaake.elmkotlin.sample.base.RxActivityLifecycle
import io.reactivex.Observable
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk21.listeners.onClick
import org.jetbrains.anko.verticalLayout

class MainActivity : BaseActivity<MainMsg, MainModel, View>() {

    override fun onCreateProgram(
            context: Context,
            state: Observable<AppModel>,
            dispatchMsg: (MainMsg) -> Unit,
            lifecycle: RxActivityLifecycle
    ): Component<MainMsg, MainModel, View> {

        val init = Reaction(initMainModel(), none())
        val view = { _: MainModel ->
            verticalLayout {
                button("Counter Example") {
                    onClick {
                        dispatchMsg(MsgClickCounterExample(this@MainActivity))
                    }
                }
            }
        }
        val update = { msg: MainMsg -> updateMap.getValue(msg.type())(msg) }
        val subscriptions = { _: MainModel -> none() }

        return createProgram(init, view, update, subscriptions)
    }
}
