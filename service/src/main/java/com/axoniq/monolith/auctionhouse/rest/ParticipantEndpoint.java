package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.AuctionDto;
import com.axoniq.monolith.auctionhouse.api.GetAllAuctionsForParticipant;
import com.axoniq.monolith.auctionhouse.api.GetAllAuctionsWithBidsForParticipant;
import com.axoniq.monolith.auctionhouse.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("participants")
public class ParticipantEndpoint {
    private final ParticipantService participantService;
    private final QueryGateway queryGateway;

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return participantService.registerAsParticipant(request.email);
    }

    @GetMapping("{id}/auctions")
    public CompletableFuture<List<AuctionDto>> getAuctionsForParticipant(@PathVariable String id) {
        return queryGateway.query(new GetAllAuctionsForParticipant(id), ResponseTypes.multipleInstancesOf(AuctionDto.class));
    }

    @GetMapping("{id}/bids")
    public CompletableFuture<List<AuctionDto>> getAuctionsWithBidsForParticipant(@PathVariable String id) {
        return queryGateway.query(new GetAllAuctionsWithBidsForParticipant(id), ResponseTypes.multipleInstancesOf(AuctionDto.class));
    }


    record CreateRequest(
            String email
    ) {
    }
}
