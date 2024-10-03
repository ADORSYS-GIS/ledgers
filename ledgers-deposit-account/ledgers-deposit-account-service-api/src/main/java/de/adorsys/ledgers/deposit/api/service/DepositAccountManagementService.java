/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.service;

import de.adorsys.ledgers.deposit.api.domain.account.*;
import de.adorsys.ledgers.deposit.api.domain.payment.AmountTO;

import de.adorsys.ledgers.deposit.api.domain.sca.ScaInfoTO;
import de.adorsys.ledgers.deposit.api.domain.account.AccountReportTO;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import de.adorsys.ledgers.util.domain.CustomPageableImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DepositAccountManagementService {

    List<AccountDetailsTO> getAccountsByIbanAndCurrency(String iban, String currency);

    /**
     * Creates a new DepositAccount. This deposit account is then linked with the specified user.
     * <p>
     * Call requires a bank staff access permission.
     *
     * @param userId:        the identifier of the user for whom the account is created
     * @param scaInfoTO      : SCA information
     * @param depositAccount : the deposit account to be crated.
     */
    boolean createDepositAccount(String userId, ScaInfoTO scaInfoTO, AccountDetailsTO depositAccount);

    /**
     * Retrieve the list of account viewable by the connected user.
     *
     * @param userId : user identifier
     * @return an empty list if user not linked with any deposit accounted.
     */
    List<AccountDetailsTO> listDepositAccounts(String userId);

    /**
     * Retrieve the list of account registered for the branch.
     *
     * @param userId : user identifier
     * @return list of accounts registered for the branch, or an empty list otherwise
     */
    List<AccountDetailsTO> listDepositAccountsByBranch(String userId);

    CustomPageImpl<AccountDetailsTO> listDepositAccountsByBranchPaged(String userId, String queryParam, boolean withBalance, CustomPageableImpl pageable);

    CustomPageImpl<AccountDetailsExtendedTO> getAccountsByBranchAndMultipleParams(String countryCode, String branchId, String branchLogin, String iban, Boolean blocked, CustomPageableImpl pageable);

    /**
     * Retrieves AccountDetails with Balance on demand
     *
     * @param id          DepositAccount identifier
     * @param time        the reference time.
     * @param withBalance boolean specifying if Balances has to be added to AccountDetails
     * @return account details.
     */
    AccountDetailsTO getDepositAccountById(String id, LocalDateTime time, boolean withBalance);

    //============================ Account Details ==============================//

    /**
     * Retrieves transaction by accountId and transactionId
     *
     * @param accountId     the account id
     * @param transactionId the transaction id
     * @return the corresponding transaction
     */
    TransactionTO getTransactionById(String accountId, String transactionId);

    /**
     * Retrieves a List of transactions by accountId and dates (from/to) if dateTo is empty it is considered that requested date is today
     *
     * @param accountId the account id
     * @param dateFrom  from this time
     * @param dateTo    to this time
     * @return : List of transactions.
     */
    List<TransactionTO> getTransactionsByDates(String accountId, LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves a List of transactions by accountId and dates (from/to) if dateTo is empty it is considered that requested date is today
     *
     * @param accountId the account id
     * @param dateFrom  from this time
     * @param dateTo    to this time
     * @return : List of transactions.
     */
    CustomPageImpl<TransactionTO> getTransactionsByDatesPaged(String accountId, LocalDate dateFrom, LocalDate dateTo, CustomPageableImpl pageable);

    
    
    void depositCash(String accountId, AmountTO amount);

    AccountReportTO getAccountReport(String accountId);

    boolean changeStatus(String accountId, boolean systemBlock);

    void changeCreditLimit(String accountId, BigDecimal creditLimit);

    boolean createDepositAccount(String userId, de.adorsys.ledgers.deposit.api.domain.account.AccountDetailsTO accountDetailsTO);
}
