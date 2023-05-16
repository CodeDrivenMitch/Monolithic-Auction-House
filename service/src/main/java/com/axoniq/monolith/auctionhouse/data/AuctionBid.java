package com.axoniq.monolith.auctionhouse.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Table(name = "monolith_bids")
@Getter
@Setter
@ToString
public class AuctionBid {
    @Id
    private String id;
    @ManyToOne
    private Auction auction;
    @ManyToOne
    private Participant participant;

    private Double bid;

    private Instant time;
}
