package xyz.brettb.qed.models

import kotlinx.serialization.*

@Serializable
data class EdStationsPage (
    val docs: List<EdStation>,
    val total: Long,
    val limit: Long,
    val page: Long,
    val pages: Long
)