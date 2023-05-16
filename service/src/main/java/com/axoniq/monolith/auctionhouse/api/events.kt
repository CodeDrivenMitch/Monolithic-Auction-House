package com.axoniq.monolith.auctionhouse.api

data class ParticipantRegistered(
    val id: String,
    val email: String,
)

data class BalanceAddedToParticipant(
    val id: String,
    val amount: Double,
    val newBalance: Double,
    val reason: String,
)

data class AuctionCreated(
    val id: String,
    val objectId: String,
    val minimumBid: Double,
)

data class AuctionEnded(
    val id: String,
    val winningBidder: String,
    val winningBid: Double,
)

data class AuctionReverted(
    val id: String,
    val reason: String,
)
data class BalanceRemovedFromParticipant(
    val email: String,
    val amount: Double,
    val newBalance: Double,
    val reason: String,
)

data class AuctionObjectRegistered(
    val id: String,
    val ownerId: String,
    val name: String,
)

data class ParticipantBidOnAuction(
    val objectId: String,
    val bidder: String,
    val bidAmount: Double,
)
