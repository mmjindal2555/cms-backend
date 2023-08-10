package com.intuit.cms.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = true)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "service_contract_number", referencedColumnName = "id", nullable = true)
    private Contract serviceContract;

    private String firstName;
    private String lastName;
    private String role;
    private long startDate;
    private long endDate;

    public Vendor(String firstName, String lastName, String role, long startDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.startDate = startDate;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getFullNameWithID() {
        return getFullName()+" ("+getId()+")";
    }
}
