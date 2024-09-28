package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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

    @Min(0)
    @Max(1)
    private double weight; // Level of access (0 to 1)

    @Enumerated(EnumType.STRING)
    private AccessStatus status;

    @ElementCollection(targetClass = TypeOfManagedAccess.class)
    @Enumerated(EnumType.STRING)
    private Set<TypeOfManagedAccess> managedAccessTypes;

    @ElementCollection(targetClass = ScopeOfManagerAccess.class)
    @Enumerated(EnumType.STRING)
    private Set<ScopeOfManagerAccess> scopeOfAccess;

}
