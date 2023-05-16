package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.data.Auction;
import com.axoniq.monolith.auctionhouse.data.AuctionObject;
import com.axoniq.monolith.auctionhouse.data.Participant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataAnalyticsService {
    public void exportCreate(Participant savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of participant: {}", savedEntity);
    }

    public void exportCreate(AuctionObject savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of auction object: {}", savedEntity);
    }

    public void exportCreate(Auction savedEntity) {
        log.info("[DATA ANALYTICS] Exporting create of auction: {}", savedEntity);
    }

    public void exportAuctionFinished(Auction auction) {
        log.info("[DATA ANALYTICS] Auction finished: {}", auction);
    }

    public void exportAuctionFailed(Auction auction) {
        log.info("[DATA ANALYTICS] Auction failed: {}", auction);
    }
}
