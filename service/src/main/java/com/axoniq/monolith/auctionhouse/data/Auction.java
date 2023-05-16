package com.axoniq.monolith.auctionhouse.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "monolith_auctions")
@Getter
@Setter
@ToString(exclude = "bids")
public class Auction {
    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    private AuctionObject itemToSell;

    private AuctionState state;
    private Double minimumBid;
    private Double currentBid;
    @ManyToOne
    private Participant currentBidder;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AuctionBid> bids;
    private Instant endTime;

}
