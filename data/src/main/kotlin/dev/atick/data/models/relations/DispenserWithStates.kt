package dev.atick.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import dev.atick.data.models.Dispenser
import dev.atick.data.models.DispenserState

data class DispenserWithStates(
    @Embedded
    val dispenser: Dispenser,
    @Relation(
        parentColumn = "device_id",
        entityColumn = "device_id"
    )
    val states: List<DispenserState>
)
