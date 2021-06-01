package com.internshala.notes.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithNotes(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userCreatorUid"
    )
    val notes: List<Note>
)