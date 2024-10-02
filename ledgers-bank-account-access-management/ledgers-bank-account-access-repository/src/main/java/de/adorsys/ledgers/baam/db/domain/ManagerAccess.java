package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;


    private double weight; // Level of access (0 to 1)

    @Enumerated(EnumType.STRING)
    private AccessStatus status;

    @ElementCollection
    private Set<TypeOfManagedAccess> managedAccessTypes = new HashSet<>();

    @ElementCollection
    private Set<AccessScope> scopeOfAccess = new HashSet<>();

}
