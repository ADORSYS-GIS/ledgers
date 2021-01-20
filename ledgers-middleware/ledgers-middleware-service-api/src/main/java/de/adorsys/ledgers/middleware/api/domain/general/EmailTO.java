package de.adorsys.ledgers.middleware.api.domain.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailTO {
    private String from;
    private String to;
    private String subject;
    private String body;
}
