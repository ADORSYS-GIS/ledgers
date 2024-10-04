package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAccess extends BankAccountAccess {

    private double weight; // Level of access (0 to 1)


    @ElementCollection
    private Set<TypeOfManagedAccess> managedAccessTypes = new HashSet<>();

    @ElementCollection
    private Set<AccessScope> scopeOfAccess = new HashSet<>();

}
