package com.colorhaake.elmkotlin.sample.counter

data class CounterModel(val count: Int)

val initCounterModel: () -> CounterModel = { CounterModel(0) }
