/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.postings.impl.converter;

import de.adorsys.ledgers.postings.api.domain.PostingBO;
import de.adorsys.ledgers.postings.db.domain.OperationDetails;
import de.adorsys.ledgers.postings.db.domain.Posting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {LedgerAccountMapper.class, LedgerMapper.class})
public interface PostingMapper {

    @Mapping(source = "posting.oprDetails.opDetails", target = "oprDetails")
    PostingBO toPostingBO(Posting posting);

    Posting toPosting(PostingBO posting);

    String toOprDetailsBO(OperationDetails operationDetails);

    @Mapping(target = "opDetails", source = "operationDetails")
    @Mapping(target = "id", expression = "java(de.adorsys.ledgers.util.Ids.id())")
    OperationDetails toOperationDetails(String operationDetails);

    List<Posting> toPostingList(List<PostingBO> postingsBO);
}
