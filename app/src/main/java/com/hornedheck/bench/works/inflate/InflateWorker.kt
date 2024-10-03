package com.hornedheck.bench.works.inflate

import android.content.Context
import android.view.LayoutInflater
import com.hornedheck.bench.R
import com.hornedheck.bench.works.Worker

class InflateWorker : Worker() {

    override val batchSize: Int
        get() = 5

    override fun run(context: Context, batchIteration: Int, iteration : Int) {
        LayoutInflater.from(context).inflate(R.layout.layout_sample, null , false)
    }
}