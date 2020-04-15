package xyz.brettb.qed.models

import kotlinx.serialization.*

@Serializable
data class EdSystem (
    @SerialName("_id")
    val id: String,

    @SerialName("id")
    val docID: Long,

    @SerialName("name_lower")
    val nameLower: String,

    @SerialName("reserve_type")
    val reserveType: String,

    @SerialName("reserve_type_id")
    val reserveTypeID: Long,

    @SerialName("controlling_minor_faction")
    val controllingMinorFaction: String,

    @SerialName("controlling_minor_faction_id")
    val controllingMinorFactionID: Long,

    @SerialName("simbad_ref")
    val simbadRef: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("needs_permit")
    val needsPermit: Boolean,

    @SerialName("power_state_id")
    val powerStateID: Long,

    @SerialName("power_state")
    val powerState: String,

    val power: String,

    @SerialName("primary_economy")
    val primaryEconomy: String,

    @SerialName("primary_economy_id")
    val primaryEconomyID: Long,

    val security: String,

    @SerialName("security_id")
    val securityID: Long,

    val allegiance: String,

    @SerialName("allegiance_id")
    val allegianceID: Long,

    val government: String,

    @SerialName("government_id")
    val governmentID: Long = 96L,

    @SerialName("is_populated")
    val isPopulated: Boolean,

    val population: Long,
    val z: Double = 0.0,
    val y: Double = 0.0,
    val x: Double = 0.0,
    val name: String,

    @SerialName("edsm_id")
    val edsmID: Long,

    @SerialName("__v")
    val v: Long
)
