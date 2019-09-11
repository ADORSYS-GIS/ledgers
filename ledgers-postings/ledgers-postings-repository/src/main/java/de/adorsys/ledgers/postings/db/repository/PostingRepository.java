package de.adorsys.ledgers.postings.db.repository;

import de.adorsys.ledgers.postings.db.domain.Ledger;
import de.adorsys.ledgers.postings.db.domain.Posting;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends PagingAndSortingRepository<Posting, String> {
    /**
     * Load the non discaded posting.
     *
     * @param oprId operation id
     * @return posting wrapped with Optional
     */
    Optional<Posting> findByOprIdAndDiscardingIdIsNull(String oprId);

    List<Posting> findByOprId(String oprId);

    Optional<Posting> findFirstByLedgerOrderByRecordTimeDesc(Ledger ledger);
}
