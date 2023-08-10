package com.intuit.cms.models.dto;

import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Contract.State;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter @NoArgsConstructor
public class ContractUpsertDTO {
    private String description;
    private Contract.State state;

    public ContractUpsertDTO(String description, State state) {
        this.description = description;
        this.state = state;
    }
}
