package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.service.AuctionObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("objects")
public class ObjectEndpoint {
    private final AuctionObjectService auctionObjectService;

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return auctionObjectService.createObject(request.name, request.owner);
    }

    record CreateRequest(
            String owner,
            String name
    ) {
    }
}
