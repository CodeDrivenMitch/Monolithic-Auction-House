package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.data.Participant;
import com.axoniq.monolith.auctionhouse.data.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository repository;
    private final DataAnalyticsService dataAnalyticsService;
    private final HubspotExporter hubspotExporter;

    public String registerAsParticipant(String email) {
        Participant participant = new Participant();
        participant.setId(UUID.randomUUID().toString());
        participant.setEmail(email);
        participant.setBalance(20.0);
        Participant savedEntity = repository.save(participant);
        dataAnalyticsService.exportCreate(savedEntity);
        hubspotExporter.registerToMailingList(email);
        return savedEntity.getId();
    }

    public Participant findParticipantById(String id) {
        return repository.findById(id).orElseThrow();
    }
}
