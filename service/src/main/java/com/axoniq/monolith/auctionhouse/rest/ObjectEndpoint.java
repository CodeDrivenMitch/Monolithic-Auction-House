package com.axoniq.monolith.auctionhouse.rest;

import com.axoniq.monolith.auctionhouse.api.RegisterObjectCommand;
import com.axoniq.monolith.auctionhouse.service.AuctionObjectService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("objects")
public class ObjectEndpoint {
    private final CommandGateway commandGateway;

    @PostMapping
    public String create(@RequestBody CreateRequest request) {
        return commandGateway.sendAndWait(new RegisterObjectCommand(request.name, request.owner));
    }

    record CreateRequest(
            String owner,
            String name
    ) {
    }
}
