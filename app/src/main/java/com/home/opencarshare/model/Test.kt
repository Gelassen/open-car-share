package com.home.opencarshare.model

class Test {
    object Status {
        const val test = 1
        const val SUCCEED = "1"
        const val FAILED = "0"
        const val NONE = "-1"
    }

    fun met(st: String?) {
        when (st) {
            Status.SUCCEED -> {}
        }
    }
}