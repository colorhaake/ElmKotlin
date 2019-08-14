package com.colorhaake.elmkotlin.sample.counter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import com.colorhaake.elmkotlin.*
import com.colorhaake.elmkotlin.sample.AppModel
import com.colorhaake.elmkotlin.sample.base.BaseActivity
import com.colorhaake.elmkotlin.sample.base.RxActivityLifecycle
import io.reactivex.Observable
import org.jetbrains.anko.button
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.sdk21.listeners.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class CounterActivity : BaseActivity<CounterMsg, CounterModel, View>() {

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onCreateProgram(
            context: Context,
            state: Observable<AppModel>,
            dispatchMsg: (CounterMsg) -> Unit,
            lifecycle: RxActivityLifecycle
    ): Component<CounterMsg, CounterModel, View> {

        val init = Reaction(initCounterModel(), none())
        val view = { model: CounterModel ->
            verticalLayout {
                textView {
                    text = "Count: ${model.count}"
                }

                linearLayout {
                    button("Sub") {
                        onClick { dispatchMsg(MsgClickSubButton()) }
                    }

                    button("Add") {
                        onClick { dispatchMsg(MsgClickAddButton() )}
                    }
                }
            }
        }
        val update = { msg: CounterMsg -> updateMap.getValue(msg.type())(msg) }
        val subscriptions = { _: CounterModel -> none() }

        state.takeUntil(lifecycle.onDestroyObserver.map { true })
                .map(AppModel::count)
                .subscribe { count -> dispatchMsg(MsgUpdateCounter(count)) }

        return createProgram(init, view, update, subscriptions)
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, CounterActivity::class.java))
        }
    }
}