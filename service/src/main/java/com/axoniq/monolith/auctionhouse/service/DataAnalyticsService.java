package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.api.*;
import com.axoniq.monolith.auctionhouse.data.Auction;
import com.axoniq.monolith.auctionhouse.data.AuctionObject;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ProcessingGroup("data-analytics")
public class DataAnalyticsService {
    @EventHandler
    public void exportCreate(ParticipantRegistered savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of participant: {}", savedEntity);
    }

    @EventHandler
    public void exportCreate(AuctionObjectRegistered savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of auction object: {}", savedEntity);
    }

    @EventHandler
    public void exportCreate(AuctionCreated savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of auction: {}", savedEntity);
    }

    @EventHandler
    public void exportAuctionFinished(AuctionEnded auction) {
        log.info("[DATA ANALYTICS] Auction finished: {}", auction);
    }

    @EventHandler
    public void exportAuctionFailed(AuctionReverted auction) {
        log.info("[DATA ANALYTICS] Auction failed: {}", auction);
    }
}
