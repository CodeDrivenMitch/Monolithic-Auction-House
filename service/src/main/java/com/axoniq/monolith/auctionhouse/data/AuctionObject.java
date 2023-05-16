package com.axoniq.monolith.auctionhouse.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "monolith_auction_objects")
@Getter
@Setter
@ToString(exclude = "auctions")
public class AuctionObject {
    @Id
    private String id;
    private String name;
    @ManyToOne
    private Participant owner;
    @OneToMany(mappedBy = "itemToSell")
    private List<Auction> auctions;
}
