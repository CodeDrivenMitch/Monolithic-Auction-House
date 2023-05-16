package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.AuctionDetailDto;
import com.axoniq.monolith.auctionhouse.api.AuctionDto;
import com.axoniq.monolith.auctionhouse.api.GetAllActiveAuctions;
import com.axoniq.monolith.auctionhouse.api.GetAuctionDetails;
import com.axoniq.monolith.auctionhouse.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("auctions")
public class AuctionEndpoint {
    private final QueryGateway queryGateway;
    private final AuctionService auctionService;

    @GetMapping("active")
    public CompletableFuture<List<AuctionDto>> getAuctions() {
        return queryGateway.query(new GetAllActiveAuctions(), ResponseTypes.multipleInstancesOf(AuctionDto.class));
    }

    @GetMapping("{id}")
    public CompletableFuture<AuctionDetailDto> getAuctionDetails(@PathVariable String id) {
        return queryGateway.query(new GetAuctionDetails(id), ResponseTypes.instanceOf(AuctionDetailDto.class));
    }

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return auctionService.createAuction(request.objectId, request.minimumBid, Instant.now().plus(request.duration, ChronoUnit.SECONDS));
    }

    @PostMapping("{id}/bid")
    public void bid(@PathVariable String id, @RequestBody BidRequest request) {
        auctionService.bidOnAuction(id, request.bid, request.participant);
    }

    record CreateRequest(
            String objectId,
            Double minimumBid,
            int duration
    ) {
    }

    record BidRequest(
            String participant,
            Double bid
    ) {
    }
}
