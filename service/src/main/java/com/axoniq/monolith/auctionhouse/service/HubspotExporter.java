package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.api.ParticipantBidOnAuction;
import com.axoniq.monolith.auctionhouse.api.ParticipantRegistered;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@ProcessingGroup("hubspot")
public class HubspotExporter {
    private final Map<String, String> idToEmailMap = new HashMap<>();

    @EventHandler
    public void handle(ParticipantRegistered event) {
        registerToMailingList(event.getEmail());
        idToEmailMap.put(event.getId(), event.getEmail());
        log.info("Got participant {}={}", event.getId(), event.getEmail());
    }

    @EventHandler
    public void handle(ParticipantBidOnAuction event) {
        log.info("Got bidder {}", event.getBidder());
        registerToMailingList(idToEmailMap.get(event.getBidder()));
    }

    private void registerToMailingList(String email) {
        if (email.startsWith("a")) {
            throw new IllegalArgumentException("No Allards allowed in Hubspot!");
        }
        log.info("[HUBSPOT] Registered to mailing list: {}", email);
    }
}
