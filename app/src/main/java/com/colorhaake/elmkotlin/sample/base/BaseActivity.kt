package com.colorhaake.elmkotlin.sample.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colorhaake.elmkotlin.Component
import com.colorhaake.elmkotlin.sample.AppModel
import com.colorhaake.elmkotlin.sample.AppMsg
import com.colorhaake.elmkotlin.sample.Application
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class RxActivityLifecycle {
    val onCreateObserver: PublishSubject<Bundle> = PublishSubject.create()
    val onStartObserver: PublishSubject<Unit> = PublishSubject.create()
    val onResumeObserver: PublishSubject<Unit> = PublishSubject.create()
    val onPauseObserver: PublishSubject<Unit> = PublishSubject.create()
    val onStopObserver: PublishSubject<Unit> = PublishSubject.create()
    val onRestartObserver: PublishSubject<Unit> = PublishSubject.create()
    val onDestroyObserver: PublishSubject<Unit> = PublishSubject.create()
    val onActivityResultObserver: PublishSubject<ActivityResultData> = PublishSubject.create()
}

data class ActivityResultData(val requestCode: Int, val resultCode: Int, val data: Intent?)

abstract class BaseActivity<Ms, Md, V> : AppCompatActivity() {

    private lateinit var component: Component<Ms, Md, V>
    private lateinit var disposables: CompositeDisposable

    private val rxLifecycle = RxActivityLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as Application).component
        val eventS = PublishSubject.create<Ms>()
        val dispatchMsg = eventS::onNext

        component = onCreateProgram(
                this, appComponent.model, dispatchMsg, rxLifecycle
        )
                .apply { subscribeAll() }

        disposables = CompositeDisposable()

        eventS.subscribe(component.dispatchMsg)
                .let(disposables::add)

        component.update.subscribe { msg ->
            (msg as? AppMsg)?.let(appComponent.dispatchMsg)
        }
                .let(disposables::add)

        savedInstanceState?.let(rxLifecycle.onCreateObserver::onNext)
    }

    override fun onStart() {
        super.onStart()
        rxLifecycle.onStartObserver.onNext(Unit)
    }

    override fun onResume() {
        super.onResume()
        rxLifecycle.onResumeObserver.onNext(Unit)
    }

    override fun onPause() {
        super.onPause()
        rxLifecycle.onPauseObserver.onNext(Unit)
    }

    override fun onStop() {
        super.onStop()
        rxLifecycle.onStopObserver.onNext(Unit)
        component.unsubscribeAll()
        disposables.clear()
    }

    override fun onRestart() {
        super.onRestart()
        rxLifecycle.onRestartObserver.onNext(Unit)
    }

    override fun onDestroy() {
        super.onDestroy()
        rxLifecycle.onDestroyObserver.onNext(Unit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        rxLifecycle.onActivityResultObserver.onNext(
                ActivityResultData(requestCode, resultCode, data)
        )
    }

    abstract fun onCreateProgram(
            context: Context,
            state: Observable<AppModel>,
            dispatchMsg: (Ms) -> Unit,
            lifecycle: RxActivityLifecycle
    ): Component<Ms, Md, V>
}