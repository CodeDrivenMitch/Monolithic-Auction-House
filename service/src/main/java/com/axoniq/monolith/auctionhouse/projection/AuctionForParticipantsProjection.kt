package com.axoniq.monolith.auctionhouse.projection

import com.axoniq.monolith.auctionhouse.api.*
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service

@ProcessingGroup("participant-auctions")
@Service
class BalanceProjection {
    private val balanceMap = mutableMapOf<String, Double>()

    @EventHandler
    fun on(event: BalanceAddedToParticipant) {
        balanceMap[event.id] = event.newBalance
    }

    @EventHandler
    fun on(event: BalanceRemovedFromParticipant) {
        balanceMap[event.id] = event.newBalance
    }

    @QueryHandler
    fun on(query: GetBalanceForParticipant): Double {
        return balanceMap[query.id] ?: 0.0
    }
}
