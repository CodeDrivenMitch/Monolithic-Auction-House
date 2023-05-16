package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.*;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("participants")
public class ParticipantEndpoint {
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return commandGateway.sendAndWait(new RegisterParticipantCommand(request.email));
    }

    @GetMapping("{id}/auctions")
    public CompletableFuture<List<AuctionDto>> getAuctionsForParticipant(@PathVariable String id) {
        return queryGateway.query(new GetAllAuctionsForParticipant(id), ResponseTypes.multipleInstancesOf(AuctionDto.class));
    }

    @GetMapping("{id}/bids")
    public CompletableFuture<List<AuctionDto>> getAuctionsWithBidsForParticipant(@PathVariable String id) {
        return queryGateway.query(new GetAllAuctionsWithBidsForParticipant(id), ResponseTypes.multipleInstancesOf(AuctionDto.class));
    }

    @GetMapping("{id}/balance")
    public CompletableFuture<Double> getBalance(@PathVariable String id) {
        return queryGateway.query(new GetBalanceForParticipant(id), ResponseTypes.instanceOf(Double.class));
    }


    record CreateRequest(
            String email
    ) {
    }
}
