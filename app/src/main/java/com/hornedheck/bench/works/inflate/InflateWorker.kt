package com.hornedheck.bench.works.inflate

import android.content.Context
import android.view.LayoutInflater
import com.hornedheck.bench.R
import com.hornedheck.bench.works.Worker

class InflateWorker : Worker() {

    override val batchCount: Int
        get() = 1
    override val batchSize: Int
        get() = 5

    override fun run(context: Context, batchNum: Int, i: Int) {
        LayoutInflater.from(context).inflate(R.layout.layout_sample, null , false)
    }
}