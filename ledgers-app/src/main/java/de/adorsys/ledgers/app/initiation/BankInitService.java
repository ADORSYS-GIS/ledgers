package de.adorsys.ledgers.app.initiation;

import de.adorsys.ledgers.app.mock.AccountBalance;
import de.adorsys.ledgers.app.mock.BulkPaymentsData;
import de.adorsys.ledgers.app.mock.MockbankInitData;
import de.adorsys.ledgers.app.mock.SinglePaymentsData;
import de.adorsys.ledgers.deposit.api.domain.AmountBO;
import de.adorsys.ledgers.deposit.api.domain.DepositAccountBO;
import de.adorsys.ledgers.deposit.api.domain.DepositAccountDetailsBO;
import de.adorsys.ledgers.deposit.api.domain.TransactionDetailsBO;
import de.adorsys.ledgers.deposit.api.service.CurrencyExchangeRatesService;
import de.adorsys.ledgers.deposit.api.service.DepositAccountInitService;
import de.adorsys.ledgers.deposit.api.service.DepositAccountService;
import de.adorsys.ledgers.deposit.api.service.DepositAccountTransactionService;
import de.adorsys.ledgers.middleware.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.middleware.api.domain.account.AccountReferenceTO;
import de.adorsys.ledgers.middleware.api.domain.payment.AmountTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTargetTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTypeTO;
import de.adorsys.ledgers.middleware.api.domain.um.AccountAccessTO;
import de.adorsys.ledgers.middleware.api.domain.um.UserRoleTO;
import de.adorsys.ledgers.middleware.api.domain.um.UserTO;
import de.adorsys.ledgers.middleware.api.service.CurrencyService;
import de.adorsys.ledgers.middleware.impl.converter.AccountDetailsMapper;
import de.adorsys.ledgers.middleware.impl.converter.UserMapper;
import de.adorsys.ledgers.um.api.domain.AccountAccessBO;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.um.api.service.UserService;
import de.adorsys.ledgers.util.exception.DepositModuleException;
import de.adorsys.ledgers.util.exception.UserManagementModuleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
public class BankInitService {
    private final Logger logger = LoggerFactory.getLogger(BankInitService.class);

    private final MockbankInitData mockbankInitData;
    private final UserService userService;
    private final UserMapper userMapper;
    private final DepositAccountInitService depositAccountInitService;
    private final DepositAccountService depositAccountService;
    private final DepositAccountTransactionService transactionService;
    private final AccountDetailsMapper accountDetailsMapper;
    private final PaymentRestInitiationService restInitiationService;
    private final CurrencyService currencyService;
    private final CurrencyExchangeRatesService exchangeRatesService;
    private final ApplicationContext context;

    private static final String ACCOUNT_NOT_FOUND_MSG = "Account {} not Found! Should never happen while initiating mock data!";
    private static final String NO_USER_BY_IBAN = "Could not get User By Iban {}! Should never happen while initiating mock data!";
    private static final LocalDateTime START_DATE = LocalDateTime.of(2018, 1, 1, 1, 1);

    @Autowired
    public BankInitService(MockbankInitData mockbankInitData, UserService userService, UserMapper userMapper,
                           DepositAccountInitService depositAccountInitService, DepositAccountService depositAccountService,
                           DepositAccountTransactionService transactionService, AccountDetailsMapper accountDetailsMapper, PaymentRestInitiationService restInitiationService,
                           CurrencyService currencyService, CurrencyExchangeRatesService exchangeRatesService, ApplicationContext context) {
        this.mockbankInitData = mockbankInitData;
        this.userService = userService;
        this.userMapper = userMapper;
        this.depositAccountInitService = depositAccountInitService;
        this.depositAccountService = depositAccountService;
        this.transactionService = transactionService;
        this.accountDetailsMapper = accountDetailsMapper;
        this.restInitiationService = restInitiationService;
        this.currencyService = currencyService;
        this.exchangeRatesService = exchangeRatesService;
        this.context = context;
    }

    public void init() {
        depositAccountInitService.initConfigData();
        createAdmin();
        try {
            exchangeRatesService.updateRates();
        } catch (IOException e) {
            log.error("ExchangeRate update failed for external service and default values on: {}, service is discontinued until Rate Service is fixed!", LocalDateTime.now());
            SpringApplication.exit(context, () -> 0);
        }
    }

    public void uploadTestData() {
        createUsers();
        createAccounts();
        performTransactions();
    }

    private void createAdmin() {
        try {
            userService.findByLogin("admin");
            logger.info("Admin user is already present. Skipping creation");
        } catch (UserManagementModuleException e) {
            UserTO admin = new UserTO("admin", "admin@example.com", "admin123");
            admin.setUserRoles(Collections.singleton(UserRoleTO.SYSTEM));
            createUser(admin);
        }
    }

    private void performTransactions() {
        List<UserTO> users = mockbankInitData.getUsers();
        performSinglePayments(users);
        performBulkPayments(users);
    }

    private void performSinglePayments(List<UserTO> users) {
        for (SinglePaymentsData paymentsData : mockbankInitData.getSinglePayments()) {
            PaymentTO payment = paymentsData.getSinglePayment();
            try {
                if (isAbsentTransactionRegular(payment.getDebtorAccount().getIban(), payment.getDebtorAccount().getCurrency(), payment.getTargets().get(0).getEndToEndIdentification())) {
                    UserTO user = getUserByIban(users, payment.getDebtorAccount().getIban());
                    restInitiationService.executePayment(user, PaymentTypeTO.SINGLE, payment);
                }
            } catch (DepositModuleException e) {
                logger.error(ACCOUNT_NOT_FOUND_MSG, payment.getDebtorAccount().getIban());
            } catch (UserManagementModuleException e) {
                logger.error(NO_USER_BY_IBAN, payment.getDebtorAccount().getIban());
            }
        }
    }

    private void performBulkPayments(List<UserTO> users) {
        for (BulkPaymentsData paymentsData : mockbankInitData.getBulkPayments()) {
            PaymentTO payment = paymentsData.getBulkPayment();
            AccountReferenceTO debtorAccount = payment.getDebtorAccount();
            try {
                boolean isAbsentTransaction;
                if (Optional.ofNullable(payment.getBatchBookingPreferred()).orElse(false)) {
                    isAbsentTransaction = isAbsentTransactionBatch(payment);
                } else {
                    isAbsentTransaction = isAbsentTransactionRegular(debtorAccount.getIban(), debtorAccount.getCurrency(), payment.getTargets().iterator().next().getEndToEndIdentification());
                }
                if (isAbsentTransaction) {
                    UserTO user = getUserByIban(users, debtorAccount.getIban());
                    restInitiationService.executePayment(user, PaymentTypeTO.BULK, payment);
                }
            } catch (DepositModuleException e) {
                logger.error(ACCOUNT_NOT_FOUND_MSG, debtorAccount.getIban());
            } catch (UserManagementModuleException e) {
                logger.error(NO_USER_BY_IBAN, debtorAccount.getIban());
            }
        }
    }

    private boolean isAbsentTransactionBatch(PaymentTO payment) {
        DepositAccountBO account = depositAccountService.getAccountByIbanAndCurrency(payment.getDebtorAccount().getIban(), payment.getDebtorAccount().getCurrency());
        List<TransactionDetailsBO> transactions = depositAccountService.getTransactionsByDates(account.getId(), START_DATE, LocalDateTime.now());
        BigDecimal total = BigDecimal.ZERO.subtract(payment.getTargets().stream()
                                                            .map(PaymentTargetTO::getInstructedAmount)
                                                            .map(AmountTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, 5));
        return transactions.stream()
                       .noneMatch(t -> t.getTransactionAmount().getAmount().equals(total));
    }

    private boolean isAbsentTransactionRegular(String iban, Currency currency, String entToEndId) {
        DepositAccountBO account = depositAccountService.getAccountByIbanAndCurrency(iban, currency);
        List<TransactionDetailsBO> transactions = depositAccountService.getTransactionsByDates(account.getId(), START_DATE, LocalDateTime.now());
        return transactions.stream()
                       .noneMatch(t -> entToEndId.equals(t.getEndToEndId()));
    }

    private UserTO getUserByIban(List<UserTO> users, String iban) {
        return users.stream()
                       .filter(user -> isAccountContainedInAccess(user.getAccountAccesses(), iban))
                       .findFirst()
                       .orElseThrow(() -> UserManagementModuleException.builder().build());
    }

    private void createAccounts() {
        for (AccountDetailsTO details : mockbankInitData.getAccounts()) {
            if (!currencyService.isCurrencyValid(details.getCurrency())) {
                throw new IllegalArgumentException("Currency is not supported: " + details.getCurrency());
            }
            DepositAccountDetailsBO account;
            try {
                account = depositAccountService.getAccountDetailsByIbanAndCurrency(details.getIban(), details.getCurrency(), LocalDateTime.now(), true);

            } catch (DepositModuleException e) {
                account = createAccount(details);
            }
            updateBalanceIfRequired(details, account);
        }
    }

    private void updateBalanceIfRequired(AccountDetailsTO details, DepositAccountDetailsBO account) {
        getBalanceFromInitData(details)
                .ifPresent(b -> checkAndUpdateBalance(details, account, b));
    }

    private void checkAndUpdateBalance(AccountDetailsTO details, DepositAccountDetailsBO account, BigDecimal balanceValue) {
        AmountBO amount = new AmountBO(details.getCurrency(), balanceValue);
        try {
            if (account.getBalances().iterator().next().getAmount().getAmount().compareTo(amount.getAmount()) < 0) {
                transactionService.depositCash(account.getAccount().getId(), amount, "SYSTEM");
            }
        } catch (DepositModuleException e) {
            logger.error("Unable to deposit cash to account: {} {}", details.getIban(), details.getCurrency());
        }
    }

    private Optional<BigDecimal> getBalanceFromInitData(AccountDetailsTO details) {
        return mockbankInitData.getBalances().stream()
                       .filter(getAccountBalancePredicate(details))
                       .findFirst()
                       .map(AccountBalance::getBalance);
    }

    @NotNull
    private Predicate<AccountBalance> getAccountBalancePredicate(AccountDetailsTO details) {
        return b -> StringUtils.equals(b.getAccNbr(), details.getIban()) && b.getCurrency().equals(details.getCurrency());
    }

    private DepositAccountDetailsBO createAccount(AccountDetailsTO details) {
        String userName = getUserNameByIban(details.getIban());
        DepositAccountBO accountBO = accountDetailsMapper.toDepositAccountBO(details);
        DepositAccountBO account = depositAccountService.createNewAccount(accountBO, userName, "");
        userService.findUsersByIban(details.getIban())
                .forEach(u -> updateAccess(u, account.getIban(), account.getId()));
        return depositAccountService.getAccountDetailsById(account.getId(), LocalDateTime.now(), true);
    }

    private void updateAccess(UserBO user, String iban, String accountId) {
        List<AccountAccessBO> accesses = user.getAccountAccesses();
        accesses.stream()
                .filter(a -> a.getIban().equals(iban)).findAny()
                .ifPresent(a -> a.setAccountId(accountId));
        userService.updateAccountAccess(user.getLogin(), accesses);
    }

    private String getUserNameByIban(String iban) {
        return mockbankInitData.getUsers().stream()
                       .filter(u -> isAccountContainedInAccess(u.getAccountAccesses(), iban))
                       .findFirst()
                       .map(UserTO::getLogin)
                       .orElseThrow(() -> UserManagementModuleException.builder().build());
    }

    private boolean isAccountContainedInAccess(List<AccountAccessTO> access, String iban) {
        return access.stream()
                       .anyMatch(a -> a.getIban().equals(iban));
    }

    private void createUsers() {
        for (UserTO user : mockbankInitData.getUsers()) {
            try {
                userService.findByLogin(user.getLogin());
            } catch (UserManagementModuleException e) {
                user.getUserRoles().add(UserRoleTO.CUSTOMER);
                createUser(user);
            }
        }
    }

    private void createUser(UserTO user) {
        try {
            userService.create(userMapper.toUserBO(user));
        } catch (UserManagementModuleException e1) {
            logger.error("User already exists! Should never happen while initiating mock data!");
        }
    }
}
