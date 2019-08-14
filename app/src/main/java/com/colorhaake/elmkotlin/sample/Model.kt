package com.colorhaake.elmkotlin.sample

import com.colorhaake.elmkotlin.*

data class AppModel(val count: Int) : Model()

val initAppModel: () -> AppModel = { AppModel(0) }

