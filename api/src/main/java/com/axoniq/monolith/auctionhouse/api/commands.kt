package com.axoniq.monolith.auctionhouse.api

import java.time.Instant

data class CreateAuctionCommand(
    val objectId: String,
    val minimumBid: Double,
    val endTime: Instant,
)

data class BidOnAuctionCommand(
    val auctionId: String,
    val bid: Double,
    val bidder: String,
)

data class RegisterParticipantCommand(
    val email: String,
)
data class RegisterObjectCommand(
    val ownerId: String,
    val name: String,
)
