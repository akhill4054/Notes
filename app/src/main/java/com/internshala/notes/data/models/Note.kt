package com.internshala.notes.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(index = true)
    val userCreatorUid: String,
    val title: String = "",
    val note: String = "",
    val createdOn: Date? = null,
    val lastModified: Date? = null,
    val deletedOn: Date? = null,
    val status: Int = NOTE_STATUS_IDEAL,
) : Parcelable {
    fun isEmptyNote(): Boolean =
        title.isEmpty() && note.isEmpty()

    fun isFreshNote(): Boolean = createdOn == null

    fun isEditable(): Boolean =
        status == NOTE_STATUS_IDEAL || status == NOTE_STATUS_ARCHIVED

    fun isArchived(): Boolean = status == NOTE_STATUS_ARCHIVED

    fun isBinned(): Boolean = status == NOTE_STATUS_DELETED

    companion object {
        const val NOTE_STATUS_IDEAL = 0
        const val NOTE_STATUS_ARCHIVED = 1
        const val NOTE_STATUS_DELETED = 2
    }
}