package com.axoniq.monolith.auctionhouse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HubspotExporter {

    public void registerToMailingList(String email) {
        if(email.startsWith("a")) {
            throw new IllegalArgumentException("No Allards allowed in Hubspot!");
        }
        log.info("[HUBSPOT] Registered to mailing list: {}", email);
    }
}
