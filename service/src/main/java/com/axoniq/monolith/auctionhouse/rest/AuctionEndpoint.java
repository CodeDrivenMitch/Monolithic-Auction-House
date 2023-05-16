package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.AuctionDetailDto;
import com.axoniq.monolith.auctionhouse.api.AuctionDto;
import com.axoniq.monolith.auctionhouse.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auctions")
public class AuctionEndpoint {
    private final AuctionService auctionService;

    @GetMapping("active")
    public List<AuctionDto> getAuctions() {
        return auctionService.getActiveAuctions();
    }

    @GetMapping("{id}")
    public AuctionDetailDto getAuctionDetails(@PathVariable String id) {
        return auctionService.getAuctionDetails(id);
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
