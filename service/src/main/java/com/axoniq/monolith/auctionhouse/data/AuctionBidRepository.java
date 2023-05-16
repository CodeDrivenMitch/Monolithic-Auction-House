package com.axoniq.monolith.auctionhouse.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionBidRepository extends CrudRepository<AuctionBid, String> {
}
