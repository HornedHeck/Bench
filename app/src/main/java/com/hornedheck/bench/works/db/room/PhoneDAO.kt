package com.hornedheck.bench.works.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PhoneDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(phone: Phone) : Long

    @Transaction
    @Query("SELECT * FROM phone WHERE id = (:phoneId) LIMIT 1")
    suspend fun read(phoneId: Int): Phone?

    @Query("UPDATE phone SET android_version = (:newAndroidVersion) WHERE id = (:phoneId)")
    suspend fun update(phoneId: Int, newAndroidVersion: String)

    @Delete
    suspend fun delete(phone: Phone)

}