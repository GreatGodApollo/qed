package xyz.brettb.qed.models

import kotlinx.serialization.*

@Serializable
data class EdItem (
    @SerialName("_id")
    val id: String,

    val name: String,

    @SerialName("name_lower")
    val nameLower: String,

    @SerialName("id")
    val itemId: Long? = null
)