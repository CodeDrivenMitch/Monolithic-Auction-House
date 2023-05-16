package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.api.BalanceAddedToParticipant;
import com.axoniq.monolith.auctionhouse.api.ParticipantRegistered;
import com.axoniq.monolith.auctionhouse.api.RegisterParticipantCommand;
import com.axoniq.monolith.auctionhouse.data.Participant;
import com.axoniq.monolith.auctionhouse.data.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository repository;
    private final EventGateway eventGateway;

    @CommandHandler
    public String registerAsParticipant(RegisterParticipantCommand cmd) {
        Participant participant = new Participant();
        participant.setId(UUID.randomUUID().toString());
        participant.setEmail(cmd.getEmail());
        participant.setBalance(20.0);
        Participant savedEntity = repository.save(participant);

        eventGateway.publish(
                new ParticipantRegistered(participant.getId(), participant.getEmail()),
                new BalanceAddedToParticipant(participant.getId(), 20.0, 20.0, "Free credits with account creation")
        );
        return savedEntity.getId();
    }

    public Participant findParticipantById(String id) {
        return repository.findById(id).orElseThrow();
    }
}
