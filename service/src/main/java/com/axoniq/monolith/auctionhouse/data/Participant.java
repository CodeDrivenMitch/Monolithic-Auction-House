package com.axoniq.monolith.auctionhouse.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "monolith_participants")
@Getter
@Setter
@ToString(exclude = "items")
public class Participant {
    @Id
    private String id;
    private String email;
    private Double balance;

    @OneToMany
    private List<AuctionObject> items;
}
