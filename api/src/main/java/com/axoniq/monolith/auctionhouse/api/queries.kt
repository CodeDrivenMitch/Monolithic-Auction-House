package com.axoniq.monolith.auctionhouse.api

class GetAllActiveAuctions
data class GetAuctionDetails(val id: String)
data class GetAllAuctionsForParticipant(val id: String)
data class GetAllAuctionsWithBidsForParticipant(val id: String)
