package com.colorhaake.elmkotlin

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface Msg {
    fun type(): String
}

open class Model

typealias View<Md, V> = (Md) -> V

typealias Cmd = Observable<*>

fun none(): Cmd = Observable.empty<Any>()
fun batch(list: List<Cmd>): Cmd = Observable.merge(list)

typealias Sub = Cmd
typealias Subscriptions<Md> = (Md) -> Sub

typealias Reaction<Md> = Pair<Md, Cmd>

typealias Update<Ms, Md> = (Ms) -> (Md) -> Reaction<Md>

@Suppress("UNCHECKED_CAST")
fun <Ms, Md> createUpdate(update: (Ms) -> (Md) -> Reaction<Md>): Update<Msg, Md> =
        { msg -> update(msg as Ms) }

// elm do not define this
data class Component<Ms, Md, V>(
        val view: Observable<V>,
        val model: Observable<Md>,
        val update: Cmd,
        val dispatchMsg: (Ms) -> Unit
) {
    private lateinit var disposable: Disposable

    fun subscribeAll() {
        disposable = Observable.merge(listOf(view, model, update)).subscribe()
    }

    fun unsubscribeAll() {
        disposable.dispose()
    }
}

inline fun <reified Ms, Md, V> createProgram(
        init: Reaction<Md>,
        noinline view: View<Md, V>,
        crossinline update: Update<Ms, Md>,
        noinline subscriptions: Subscriptions<Md>
): Component<Ms, Md, V> {

    val eventS = PublishSubject.create<Ms>()
    val dispatchMsg = eventS::onNext

    val updateS = eventS
            .scan(init) { (model, _), msg -> update(msg)(model) }
            .share()

    val modelS = updateS.map { (model, _) -> model }

    val viewS = modelS.observeOn(AndroidSchedulers.mainThread()).map(view)

    val effectS = updateS.flatMap {
        (_, effectS) -> effectS.delay(10, TimeUnit.MILLISECONDS)
    }
    val subS = modelS.switchMap(subscriptions)

    @Suppress("UNCHECKED_CAST")
    val allUpdateS = Observable.merge(listOf(init.second, effectS, subS))
            // this is side effect. try to remove this
            .doOnNext { (it as? Ms)?.let(dispatchMsg) }

    return Component(viewS, modelS, allUpdateS, dispatchMsg)
}

/*
 * Example:
 * data class MsgA(val a: String) : Msg()
 * data class MsgB(val a: String, val b: String) : Msg()
 * data class MsgC(val a: String, val b: String, val c: String) : Msg()
 *
 * data class AppModel(val count: Int) : Model()
 *
 * val update: Update<Msg, AppModel> = createUpdate { msg -> { model -> Pair(model, batch(listOf())) } }
 * val updateA: Update<MsgA, AppModel> = createUpdate { msg -> { model -> Pair(model, batch(listOf())) } }
 * val updateB: Update<MsgB, AppModel> = createUpdate { msg -> { model -> Pair(model, batch(listOf())) } }
 * val updateC: Update<MsgC, AppModel> = createUpdate { msg -> { model -> Pair(model, batch(listOf())) } }
 *
 * val list: List<Update<out Msg, AppModel>> = listOf(update, updateA, updateB, updateC)
 */
