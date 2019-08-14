package com.colorhaake.elmkotlin.sample.base

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.colorhaake.elmkotlin.Component
import io.reactivex.subjects.PublishSubject

class RxApplicationLifecycle {
    val onCreateObserver: PublishSubject<Unit> = PublishSubject.create()
    val onStartObserver: PublishSubject<Unit> = PublishSubject.create()
    val onResumeObserver: PublishSubject<Unit> = PublishSubject.create()
    val onPauseObserver: PublishSubject<Unit> = PublishSubject.create()
    val onStopObserver: PublishSubject<Unit> = PublishSubject.create()
    val onDestroyObserver: PublishSubject<Unit> = PublishSubject.create()
    val onAnyObserver: PublishSubject<Unit> = PublishSubject.create()
}

abstract class BaseApplication<Ms, Md, V> : Application(), LifecycleObserver {
    private val rxLifecycle = RxApplicationLifecycle()

    lateinit var component: Component<Ms, Md, V>

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        super.onCreate()
        component = onCreateProgram(rxLifecycle).apply { subscribeAll() }
        rxLifecycle.onCreateObserver.onNext(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        rxLifecycle.onStartObserver.onNext(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rxLifecycle.onResumeObserver.onNext(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        rxLifecycle.onPauseObserver.onNext(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        rxLifecycle.onStopObserver.onNext(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rxLifecycle.onDestroyObserver.onNext(Unit)
        component.unsubscribeAll()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny() {
        rxLifecycle.onAnyObserver.onNext(Unit)
    }

    abstract fun onCreateProgram(lifecycle: RxApplicationLifecycle): Component<Ms, Md, V>
}