package com.axoniq.monolith.auctionhouse.service;

import com.axoniq.monolith.auctionhouse.api.*;
import com.axoniq.monolith.auctionhouse.data.*;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository repository;
    private final AuctionObjectService objectService;
    private final JobScheduler jobScheduler;
    private final ParticipantService participantService;
    private final EventGateway eventGateway;

    public void bidOnAuction(String auctionId, Double bid, String bidder) {
        Auction auction = findAuctionById(auctionId);
        if (!auction.getState().equals(AuctionState.STARTED)) {
            throw new IllegalStateException("Auction is no longer active!");
        }
        Participant bidderParticipant = participantService.findParticipantById(bidder);

        auction.setCurrentBid(bid);
        auction.setCurrentBidder(bidderParticipant);
        AuctionBid auctionBid = new AuctionBid();
        auctionBid.setBid(bid);
        auctionBid.setTime(Instant.now());
        auctionBid.setAuction(auction);
        auctionBid.setId(UUID.randomUUID().toString());
        auctionBid.setParticipant(bidderParticipant);

        auction.getBids().add(auctionBid);
        repository.save(auction);

        eventGateway.publish(
                new ParticipantBidOnAuction(auction.getItemToSell().getId(), bidder, bid)
        );
    }

    public String createAuction(String objectId, Double minimumBid, Instant endTime) {
        AuctionObject objectById = objectService.findObjectById(objectId);
        if (repository.existsByStateAndItemToSell(AuctionState.STARTED, objectById)) {
            throw new IllegalStateException("An active auction already exists for object " + objectById.getName());
        }
        Auction auction = new Auction();
        auction.setId(UUID.randomUUID().toString());
        auction.setState(AuctionState.STARTED);
        auction.setMinimumBid(minimumBid);
        auction.setItemToSell(objectById);
        auction.setEndTime(endTime);

        Auction savedEntity = repository.save(auction);
        jobScheduler.schedule(endTime, () -> endAuction(auction.getId()));

        eventGateway.publish(
                new AuctionCreated(auction.getId(), objectId, minimumBid)
        );

        return savedEntity.getId();
    }

    public void endAuction(String auctionId) {
        Auction auction = repository.findById(auctionId).orElseThrow();
        if (auction.getState().equals(AuctionState.ENDED)) {
            // Already ended
            return;
        }
        Participant bidder = auction.getCurrentBidder();
        if (bidder != null && bidder.getBalance() >= auction.getCurrentBid()) {
            auction.setState(AuctionState.ENDED);
            auction.getItemToSell().setOwner(bidder);
            bidder.setBalance(bidder.getBalance() - auction.getCurrentBid());
            Participant owner = auction.getItemToSell().getOwner();
            owner.setBalance(owner.getBalance() + auction.getCurrentBid());


            eventGateway.publish(
                    new AuctionEnded(auction.getId(), bidder.getId(), auction.getCurrentBid()),
                    new BalanceAddedToParticipant(owner.getId(), auction.getCurrentBid(), owner.getBalance() + auction.getCurrentBid(), "Sold item in auction: " + auctionId),
                    new BalanceRemovedFromParticipant(bidder.getId(), auction.getCurrentBid(), bidder.getBalance() - auction.getCurrentBid(), "Bought item in auction: " + auctionId)
            );
        } else {
            auction.setState(AuctionState.REVERTED);
            if (bidder == null) {
                eventGateway.publish(new AuctionReverted(auctionId, "No bidder!"));
            } else {
                eventGateway.publish(new AuctionReverted(auctionId, "Not enough balance on buyer's account!"));
            }
        }
        repository.save(auction);
    }

    Auction findAuctionById(String id) {
        return repository.findById(id).orElseThrow();
    }

    public List<AuctionDto> getActiveAuctions() {
        return repository.findAllByState(AuctionState.STARTED)
                .stream()
                .map(this::toOverviewDto)
                .toList();
    }

    public AuctionDetailDto getAuctionDetails(String id) {
        return toDetailDto(findAuctionById(id));
    }

    public List<AuctionDto> findAuctionsForParticipant(Participant participantById) {
        return repository.findAllBySeller(participantById.getId())
                .stream()
                .map(this::toOverviewDto)
                .toList();
    }

    public List<AuctionDto> findAuctionsWithBidsForParticipant(Participant participantById) {
        return repository.findAllByBidder(participantById.getId())
                .stream()
                .map(this::toOverviewDto)
                .toList();
    }

    private AuctionDto toOverviewDto(Auction auction) {
        Participant currentBidder = auction.getCurrentBidder();
        AuctionObject itemToSell = auction.getItemToSell();
        return new AuctionDto(
                auction.getId(),
                toAuctionObjectDto(itemToSell),
                toDtoState(auction.getState()),
                auction.getCurrentBid(),
                toParticipantDto(currentBidder),
                auction.getEndTime()
        );
    }

    private AuctionDetailDto toDetailDto(Auction auction) {
        Participant currentBidder = auction.getCurrentBidder();
        AuctionObject itemToSell = auction.getItemToSell();
        return new AuctionDetailDto(
                auction.getId(),
                toAuctionObjectDto(itemToSell),
                toDtoState(auction.getState()),
                auction.getCurrentBid(),
                toParticipantDto(currentBidder),
                auction.getEndTime(),
                auction.getBids().stream().map(this::toBidDto).toList()
        );
    }

    private BidDto toBidDto(AuctionBid auctionBid) {
        return new BidDto(auctionBid.getBid(), toParticipantDto(auctionBid.getParticipant()), auctionBid.getTime());
    }

    private static AuctionObjectDto toAuctionObjectDto(AuctionObject itemToSell) {
        return new AuctionObjectDto(itemToSell.getId(), itemToSell.getName());
    }

    private static ParticipantDto toParticipantDto(Participant currentBidder) {
        if (currentBidder == null) {
            return null;
        }
        return new ParticipantDto(currentBidder.getId(), currentBidder.getEmail());
    }

    private ActiveAuctionState toDtoState(AuctionState state) {
        switch (state) {

            case INACTIVE -> {
                return ActiveAuctionState.INACTIVE;
            }
            case STARTED -> {
                return ActiveAuctionState.STARTED;
            }
            case ENDED -> {
                return ActiveAuctionState.ENDED;
            }
            case REVERTED -> {
                return ActiveAuctionState.REVERTED;
            }
        }
        throw new IllegalStateException("No known constant");
    }
}
