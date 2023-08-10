package com.intuit.cms.models;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private String role;
    private Scope[] accessScopes;


    private long startDate;
    private long endDate;

    public enum Scope {
        ADMIN,
        SERVICE_OWNER,
        VENDOR
    }

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
        this.accessScopes = new Scope[]{};
    }

    public Employee(String name, String role, Scope[] accessScopes, long startDate) {
        this.name = name;
        this.role = role;
        this.accessScopes = accessScopes;
        this.startDate = startDate;
    }

    public boolean isAdmin(){
        return Arrays.asList(this.accessScopes).contains(Scope.ADMIN);
    }

    public boolean isServiceOwner(){
        return Arrays.asList(this.accessScopes).contains(Scope.SERVICE_OWNER);
    }
}
