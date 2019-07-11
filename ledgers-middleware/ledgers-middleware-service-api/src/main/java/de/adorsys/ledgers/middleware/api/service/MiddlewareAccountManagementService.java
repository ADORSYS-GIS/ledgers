package de.adorsys.ledgers.middleware.api.service;

import de.adorsys.ledgers.middleware.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.middleware.api.domain.account.FundsConfirmationRequestTO;
import de.adorsys.ledgers.middleware.api.domain.account.TransactionTO;
import de.adorsys.ledgers.middleware.api.domain.payment.AmountTO;
import de.adorsys.ledgers.middleware.api.domain.sca.SCAConsentResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaInfoTO;
import de.adorsys.ledgers.middleware.api.domain.um.AccountAccessTO;
import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;
import de.adorsys.ledgers.middleware.api.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MiddlewareAccountManagementService {

    /**
     * Creates a new DepositAccount. This deposit account is then linked with the specified user.
     * <p>
     * Call requires a bank staff access permission.
     *
     * @param userId:        the identifier of the user for whom the account is created
     * @param scaInfoTO      : SCA information
     * @param depositAccount : the deposit account to be crated.
     */
    void createDepositAccount(String userId, ScaInfoTO scaInfoTO, AccountDetailsTO depositAccount)
            throws AccountNotFoundMiddlewareException;

    /**
     * Creates a new DepositAccount for the connected user.
     *
     * @param scaInfoTO           : SCA information
     * @param accountNumberPrefix : the account number prefix : the account number prefix
     * @param accountNumberSuffix : th eaccount number suffix
     * @param accDetails          : account to create.
     * @throws AccountWithPrefixGoneMiddlewareException   : another user owns this prefic
     * @throws AccountWithSuffixExistsMiddlewareException : user has account with same prefix and this suffix
     */
    void createDepositAccount(ScaInfoTO scaInfoTO, String accountNumberPrefix, String accountNumberSuffix, AccountDetailsTO accDetails)
            throws AccountWithPrefixGoneMiddlewareException, AccountWithSuffixExistsMiddlewareException, AccountNotFoundMiddlewareException;

    /**
     * Retrieve the list of account viewable by the connected user.
     *
     * @param userId : user identifier
     * @return an empty list if user not linked with any deposit accounted.
     */
    List<AccountDetailsTO> listDepositAccounts(String userId);

    /**
     * TODO: return account or account details ???
     * Retrieve the list of account registered for the branch.
     *
     * @param userId : user identifier
     * @return list of accounts registered for the branch, or an empty list otherwise
     */
    List<AccountDetailsTO> listDepositAccountsByBranch(String userId);

    /**
     * Retrieves AccountDetails with Balance on demand
     *
     * @param id          DepositAccount identifier
     * @param time        the reference time.
     * @param withBalance boolean specifying if Balances has to be added to AccountDetails
     * @return account details.
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    AccountDetailsTO getDepositAccountById(String id, LocalDateTime time, boolean withBalance) throws AccountNotFoundMiddlewareException;

    /**
     * Retrieves AccountDetails with Balance on demand
     *
     * @param iban        DepositAccount iban
     * @param time        the reference time.
     * @param withBalance boolean specifying if Balances has to be added to AccountDetails
     * @return account details.
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    AccountDetailsTO getDepositAccountByIban(String iban, LocalDateTime time, boolean withBalance) throws AccountNotFoundMiddlewareException;

    //============================ Account Details ==============================//

    /**
     * Retrieves a List of AccountDetails by user login (psuId)
     *
     * @param userLogin the user login
     * @return list of account details.
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    List<AccountDetailsTO> getAllAccountDetailsByUserLogin(String userLogin) throws AccountNotFoundMiddlewareException;

    /**
     * Retrieves transaction by accountId and transactionId
     *
     * @param accountId     the account id
     * @param transactionId the transaction id
     * @return the corresponding transaction
     * @throws AccountNotFoundMiddlewareException     : target account not found.
     * @throws TransactionNotFoundMiddlewareException : no transaction with this id.
     */
    TransactionTO getTransactionById(String accountId, String transactionId) throws AccountNotFoundMiddlewareException, TransactionNotFoundMiddlewareException;

    /**
     * Retrieves a List of transactions by accountId and dates (from/to) if dateTo is empty it is considered that requested date is today
     *
     * @param accountId the account id
     * @param dateFrom  from this time
     * @param dateTo    to this time
     * @return : List of transactions.
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    List<TransactionTO> getTransactionsByDates(String accountId, LocalDate dateFrom, LocalDate dateTo) throws AccountNotFoundMiddlewareException;

    /**
     * Confirm the availability of funds on user account to perform the operation with specified amount
     *
     * @param request : teh fund confirmation request.
     * @return : true if fund available else false.
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    boolean confirmFundsAvailability(FundsConfirmationRequestTO request) throws AccountNotFoundMiddlewareException;

    String iban(String id);

    // ======================= CONSENT ======================//

    /**
     * Start an account consent process.
     *
     * @param scaInfoTO  SCA information
     * @param consentId  : the cosent id.
     * @param aisConsent : the consent details
     * @return the corresponding access token describing the account access
     */
    SCAConsentResponseTO startSCA(ScaInfoTO scaInfoTO, String consentId, AisConsentTO aisConsent);

    SCAConsentResponseTO loadSCAForAisConsent(String userId, String consentId, String authorisationId) throws SCAOperationExpiredMiddlewareException, AisConsentNotFoundMiddlewareException;

    SCAConsentResponseTO selectSCAMethodForAisConsent(String userId, String consentId, String authorisationId, String scaMethodId) throws PaymentNotFoundMiddlewareException;

    /**
     * Authorizes a consent request. If the authentication is completed, the returned response will contain a valid bearer token.
     *
     * @param scaInfoTO : SCA information
     * @param consentId : the cosent id
     * @return SCAConsentResponseTO : the consent response.
     * @throws AisConsentNotFoundMiddlewareException : consent not found.
     */
    SCAConsentResponseTO authorizeConsent(ScaInfoTO scaInfoTO, String consentId)
            throws AisConsentNotFoundMiddlewareException, SCAOperationValidationMiddlewareException;

    /**
     * Provide a third party provider with necessary permission to read accounts and
     * transaction information for the specified account.
     *
     * @param scaInfoTO  : SCA information
     * @param aisConsent : the consent details
     * @return the corresponding access token describing the account access
     * @throws AccountNotFoundMiddlewareException : target account not found.
     */
    SCAConsentResponseTO grantAisConsent(ScaInfoTO scaInfoTO, AisConsentTO aisConsent)
            throws AccountNotFoundMiddlewareException;

    /**
     * Deposits given amount in cash into specified account.
     * On the bank's books, the bank debits its cash account for the given amount in cash,
     * and credits a "deposits" liability account for an equal amount.
     *
     * @param scaInfoTO SCA information
     * @param accountId id of the account deposited into
     * @param amount    amount of cash deposited
     * @throws AccountNotFoundMiddlewareException target account not found
     */
    void depositCash(ScaInfoTO scaInfoTO, String accountId, AmountTO amount) throws AccountNotFoundMiddlewareException;

    /**
     * Retrieves a List of AccountAccessTO by userId
     *
     * @param userId id of the user
     */
    List<AccountAccessTO> getAccountAccesses(String userId);
}
