package de.adorsys.ledgers.um.db.repository;

import de.adorsys.ledgers.um.db.domain.OauthCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OauthCodeRepository extends CrudRepository<OauthCodeEntity, Long> {
    Optional<OauthCodeEntity> findByCodeAndUsed(String code, boolean used);

    Optional<OauthCodeEntity> findByUserId(String userId);
}
