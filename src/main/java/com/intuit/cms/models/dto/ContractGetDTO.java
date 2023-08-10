package com.intuit.cms.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter @NoArgsConstructor
public class ContractGetDTO {
    private String description;
    private List<String> assignedVendors;
    private String owner;
    private String currentState;
    private Long id;

    public ContractGetDTO(String description, List<String> assignedVendors, String owner, String currentState, Long id) {
        this.description = description;
        this.assignedVendors = assignedVendors;
        this.owner = owner;
        this.currentState = currentState;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        return true;
        if (!(o instanceof ContractGetDTO))
        return false;
        ContractGetDTO c = (ContractGetDTO) o;
        return Objects.equals(this.id, c.id) && Objects.equals(this.description, c.description)
            && Objects.equals(this.owner, c.owner) && Objects.equals(this.currentState, c.currentState)
            && Objects.equals(this.assignedVendors, c.assignedVendors);
    }
}
