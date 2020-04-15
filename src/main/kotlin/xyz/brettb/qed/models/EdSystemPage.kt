package xyz.brettb.qed.models

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json    = Json(JsonConfiguration.Stable)
// val page    = json.parse(EdSystemPage.serializer(), jsonString)

import kotlinx.serialization.*

@Serializable
data class EdSystemPage (
    val docs: List<EdSystem>,
    val total: Long,
    val limit: Long,
    val page: Long,
    val pages: Long
)