package com.hornedheck.bench.works

sealed class State {

    data object Ready : State()

    class Progress(
        val workerName : String
    ) : State()

    data class Results(
        val runResult : String
    ) : State()
}