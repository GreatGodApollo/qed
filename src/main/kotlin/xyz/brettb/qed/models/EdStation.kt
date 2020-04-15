package xyz.brettb.qed.models

import kotlinx.serialization.*

@Serializable
data class EdStation (
    @SerialName("_id")
    val id: String,

    @SerialName("id")
    val docID: Long,

    @SerialName("name_lower")
    val nameLower: String,

    @SerialName("controlling_minor_faction_id")
    val controllingMinorFactionID: Long,

    @SerialName("body_id")
    val bodyID: Long?,

    @SerialName("settlement_security")
    val settlementSecurity: String?,

    @SerialName("settlement_security_id")
    val settlementSecurityID: Long?,

    @SerialName("settlement_size")
    val settlementSize: String?,

    @SerialName("settlement_size_id")
    val settlementSizeID: Long?,

    @SerialName("selling_modules")
    val sellingModules: List<Long>,

    @SerialName("selling_ships")
    val sellingShips: List<EdItem>,

    @SerialName("is_planetary")
    val isPlanetary: Boolean,

    @SerialName("market_updated_at")
    val marketUpdatedAt: String?,

    @SerialName("outfitting_updated_at")
    val outfittingUpdatedAt: String?,

    @SerialName("shipyard_updated_at")
    val shipyardUpdatedAt: String?,

    val economies: List<EdItem>,

    @SerialName("prohibited_commodities")
    val prohibitedCommodities: List<EdItem>,

    @SerialName("export_commodities")
    val exportCommodities: List<EdItem>,

    @SerialName("import_commodities")
    val importCommodities: List<EdItem>,

    @SerialName("has_commodities")
    val hasCommodities: Boolean,

    @SerialName("has_docking")
    val hasDocking: Boolean,

    @SerialName("has_shipyard")
    val hasShipyard: Boolean,

    @SerialName("has_outfitting")
    val hasOutfitting: Boolean,

    @SerialName("has_rearm")
    val hasRearm: Boolean,

    @SerialName("has_repair")
    val hasRepair: Boolean,

    @SerialName("has_refuel")
    val hasRefuel: Boolean,

    @SerialName("has_market")
    val hasMarket: Boolean,

    @SerialName("has_blackmarket")
    val hasBlackmarket: Boolean,

    val type: String,

    @SerialName("type_id")
    val typeID: Long,

    val states: List<EdItem>,
    val allegiance: String,

    @SerialName("allegiance_id")
    val allegianceID: Long,

    val government: String,

    @SerialName("government_id")
    val governmentID: Long,

    @SerialName("distance_to_star")
    val distanceToStar: Long,

    @SerialName("max_landing_pad_size")
    val maxLandingPadSize: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("system_id")
    val systemID: Long,

    val name: String,

    @SerialName("__v")
    val v: Long
)