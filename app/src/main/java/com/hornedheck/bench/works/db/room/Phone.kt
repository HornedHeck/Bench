package com.hornedheck.bench.works.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Phone(
    @ColumnInfo(name = "model") val model : String,
    @ColumnInfo(name = "vendor") val vendor : String,
    @ColumnInfo(name = "release_date") val releaseDate : Long,
    @ColumnInfo(name = "ram_gb") val  ram : Int,
    @ColumnInfo(name = "cpu_cores") val cores : Int,
    @ColumnInfo(name = "batter_mah") val batteryCapacityMah : Int,
    @ColumnInfo(name = "android_version") val androidVersion : String,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
)