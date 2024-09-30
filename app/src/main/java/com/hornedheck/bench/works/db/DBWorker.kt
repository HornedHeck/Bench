package com.hornedheck.bench.works.db

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hornedheck.bench.works.FixedTimeWorker
import com.hornedheck.bench.works.db.room.Phone
import com.hornedheck.bench.works.db.room.RoomDb
import kotlinx.coroutines.Dispatchers

class DBWorker(timeToRun: Long, application: Application) : FixedTimeWorker(timeToRun) {



    private val instances = creteInstances()
    private val db: RoomDb = Room.databaseBuilder(application, RoomDb::class.java, "bench_phone_db")
        .enableMultiInstanceInvalidation()
        .setJournalMode(RoomDatabase.JournalMode.AUTOMATIC)
        .build()

    private val dbHelper = db.phoneDAO()

    override val context = Dispatchers.IO

    override fun prepare(context: Context) {
        db.clearAllTables()
    }

    override suspend fun runBatch(context: Context) {
        instances.forEach {
            val id = dbHelper.create(it).toInt()
            dbHelper.read(id)?.let { dbValue ->
                dbHelper.update(dbValue.id, "Android 15")
                dbHelper.delete(dbValue)
            }
        }
    }

    private fun creteInstances() = listOf(
        Phone("Nexus 5", "Google", 123456789L, 2, 6, 2300, "Android 4.4"),
        Phone("Pixel 6", "Google", 746545352L, 8, 8, 4564, "Android 6"),
        Phone("Galaxy Note 3", "Samsung", 185726696L, 4, 8, 4500, "Android 13"),
        Phone("Galaxy A53", "Samsung", 154951421L, 2, 6, 6000, "Android 8"),
        Phone("9T", "Redmi", 321984756L, 2, 4, 1500, "Android 7.1"),
        Phone("Rog Phone 4", "Asus", 455675585L, 16, 10, 950, "Android 4.4"),
        Phone("X3", "Poco", 1241333422L, 5, 6, 3000, "Android 10"),
        Phone("Note 10+", "Xiaomi", 998532355L, 8, 6, 4530, "Android 5.0"),
        Phone("Moto T10", "Motorola", 12123524L, 2, 6, 2500, "Android 4.1.2"),
        Phone("2", "Nothing Phone", 12132445L, 3, 6, 2300, "Android 11"),
    )
}

