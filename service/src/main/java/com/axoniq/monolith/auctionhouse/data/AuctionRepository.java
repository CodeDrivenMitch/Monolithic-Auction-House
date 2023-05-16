package com.axoniq.monolith.auctionhouse.data;

import com.axoniq.monolith.auctionhouse.api.AuctionDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends CrudRepository<Auction, String> {
    boolean existsByStateAndItemToSell(AuctionState state, AuctionObject itemToSell);

    List<Auction> findAllByState(AuctionState state);

    @Query("select a from Auction a where a.itemToSell.owner=:participant")
    List<Auction> findAllBySeller(@Param("participant") Participant participantById);

    @Query("select a from Auction a join a.bids b where b.participant=:participant")
    List<Auction> findAllByBidder(@Param("participant") Participant participantById);
}
