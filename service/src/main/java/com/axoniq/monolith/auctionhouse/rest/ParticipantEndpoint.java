package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.AuctionDto;
import com.axoniq.monolith.auctionhouse.data.Participant;
import com.axoniq.monolith.auctionhouse.service.AuctionService;
import com.axoniq.monolith.auctionhouse.service.ParticipantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("participants")
@Transactional
public class ParticipantEndpoint {
    private final ParticipantService participantService;
    private final AuctionService auctionService;

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return participantService.registerAsParticipant(request.email);
    }

    @GetMapping("{id}/auctions")
    public List<AuctionDto> getAuctionsForParticipant(@PathVariable String id) {
        Participant participantById = participantService.findParticipantById(id);
        return auctionService.findAuctionsForParticipant(participantById);
    }

    @GetMapping("{id}/bids")
    public List<AuctionDto> getAuctionsWithBidsForParticipant(@PathVariable String id) {
        Participant participantById = participantService.findParticipantById(id);
        return auctionService.findAuctionsWithBidsForParticipant(participantById);
    }


    record CreateRequest(
            String email
    ) {
    }
}
