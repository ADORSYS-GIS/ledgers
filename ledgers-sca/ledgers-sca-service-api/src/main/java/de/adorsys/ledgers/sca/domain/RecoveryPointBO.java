package de.adorsys.ledgers.sca.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class RecoveryPointBO {
    private Long id;
    private String description;
    private LocalDateTime rollBackTime;
    private String branchId;

    public void checkAndUpdateDescription() {
        if (StringUtils.isBlank(this.description)) {
            this.description = this.getBranchId() +
                                       " " +
                                       LocalDateTime.now() +
                                       " by " +
                                       ZonedDateTime.now().getZone() +
                                       " time.";
        }
    }
}
