package com.hornedheck.bench.works.db.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Phone::class])
abstract class RoomDb : RoomDatabase() {

    abstract fun phoneDAO() : PhoneDAO

}