package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.api.AuctionObjectRegistered;
import com.axoniq.monolith.auctionhouse.data.AuctionObject;
import com.axoniq.monolith.auctionhouse.data.AuctionObjectRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuctionObjectService {
    private final AuctionObjectRepository repository;
    private final ParticipantService participantService;
    private final EventGateway eventGateway;

    public String createObject(String name, String owner) {
        AuctionObject auctionObject = new AuctionObject();
        auctionObject.setId(UUID.randomUUID().toString());
        auctionObject.setName(name);

        var ownerEntity = participantService.findParticipantById(owner);
        auctionObject.setOwner(ownerEntity);

        var savedEntity = repository.save(auctionObject);

        eventGateway.publish(new AuctionObjectRegistered(auctionObject.getId(), owner, name));

        return savedEntity.getId();
    }

    public AuctionObject findObjectById(String id) {
        return repository.findById(id).orElseThrow();
    }
}
