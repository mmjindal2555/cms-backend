package com.intuit.cms.models;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_contract_owner_id", referencedColumnName = "id")
    private Employee serviceContractOwner;

    @OneToMany(mappedBy="serviceContract")
    private Set<Vendor> assignedVendors = new HashSet<>();

    private String description;
    private State state;

    public enum State {
        DRAFT,
        APPROVED,
        ACTIVE,
        INACTIVE
    }

    public Contract(String description, Employee serviceOwner){
        this.description = description;
        this.serviceContractOwner = serviceOwner;
        this.state = State.DRAFT;
    }
}
