package de.adorsys.ledgers.postings.db.domain;

import de.adorsys.ledgers.util.Ids;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class OperationDetails {
    @Id
    private String id;
    @Lob
    private String opDetails;

    public OperationDetails(String opDetails) {
        this.id = Ids.id();
        this.opDetails = opDetails;
    }
}
