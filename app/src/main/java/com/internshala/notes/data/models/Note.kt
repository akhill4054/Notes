package com.internshala.notes.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userCreatorId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(index = true)
    val userCreatorId: Long,
    val title: String = "",
    val note: String,
    val lastModified: Date,
    // 0 -> Ideal, 1 -> Archived, 2 -> Deleted
    val status: Int = NOTE_STATUS_IDEAL,
) {

    companion object {
        const val NOTE_STATUS_IDEAL = 0
        const val NOTE_STATUS_ARCHIVED = 1
        const val NOTE_STATUS_DELETED = 2
    }
}