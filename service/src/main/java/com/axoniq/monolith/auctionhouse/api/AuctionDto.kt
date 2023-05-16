package com.axoniq.monolith.auctionhouse.api;

import java.time.Instant

data class AuctionDetailDto(
    val identifier: String,
    val auctionObject: AuctionObjectDto,
    val state: ActiveAuctionState,
    val currentBid: Double?,
    val currentBidder: ParticipantDto?,
    val endTime: Instant,
    val bids: List<BidDto>
)

data class BidDto(
    val bid: Double?,
    val bidder: ParticipantDto?,
    val time: Instant,
)

data class AuctionDto(
    val identifier: String,
    val auctionObject: AuctionObjectDto,
    val state: ActiveAuctionState,
    val currentBid: Double?,
    val currentBidder: ParticipantDto?,
    val endTime: Instant,
)

data class AuctionObjectDto(
    val identifier: String,
    val name: String,
)

data class ParticipantDto(
    val identifier: String,
    val email: String,
)

data class ActiveAuctionsResponse(
    val auctions: List<AuctionDto>
)
