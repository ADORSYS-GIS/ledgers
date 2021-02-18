package de.adorsys.keycloak.connector.aspsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.keycloak.otp.core.domain.CodeValidationResult;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.middleware.api.domain.account.AccountReferenceTO;
import de.adorsys.ledgers.middleware.api.domain.general.AddressTO;
import de.adorsys.ledgers.middleware.api.domain.payment.*;
import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaStatusTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LedgersConnectorImplTest {
    private HttpClient client = HttpClients.createDefault();
    private static final String LEDGERS_BASE_URL = "http://localhost:8088/";

    //@Test
    void getMethods() throws IOException {
        List<ScaUserDataTO> list = SimpleHttp.doGet(LEDGERS_BASE_URL + "fapi/methods/" + "anton.brueckner", client)
                                           .asJson(AspspMapper.SCA_DATA_LIST_TYPE);
        List<ScaMethod> scaMethods = AspspMapper.mapToScaMethodList(list);
        assertEquals(1, scaMethods.size());
        assertEquals("zujftKAGSx0s1awO2vK8jg", scaMethods.get(0).getId());
        assertEquals("anton.brueckner@mail.de", scaMethods.get(0).getDescription());
        assertEquals("EMAIL", scaMethods.get(0).getType());
    }

    //@Test
    void initObjPmt() throws IOException {
        PaymentTO pmt = getPayment();
        ConfirmationObject<PaymentTO> object = new ConfirmationObject<>();
        object.setRawBusinessObject(pmt);
        object.setObjType("payment");
        object.setId("qwe");
        OpTypeTO type = AspspMapper.mapOperationType(object.getObjType());
        String path = type == OpTypeTO.CONSENT ? "consent" : "payment";
        String login = "anton.brueckner";

        int status = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/" + path + "/" + login, client)
                             .json(object.getRawBusinessObject())
                             .asStatus();

        assertEquals(202, status);
    }

    private PaymentTO getPayment() {
        PaymentTO to = new PaymentTO();
        to.setPaymentId("qwe");
        to.setDebtorAccount(new AccountReferenceTO("DE80760700240271232400", null, null, null, null, Currency.getInstance("EUR")));
        to.setPaymentProduct("sepa-credit-transfers");
        to.setPaymentType(PaymentTypeTO.SINGLE);
        to.setTransactionStatus(TransactionStatusTO.RCVD);
        PaymentTargetTO target = new PaymentTargetTO();
        target.setPaymentId("qwe");
        target.setCreditorAccount(new AccountReferenceTO("DE80760700240271232400", null, null, null, null, Currency.getInstance("EUR")));
        target.setCreditorAddress(new AddressTO());
        target.setCreditorName("BRUCKNER!!!");
        target.setInstructedAmount(new AmountTO(Currency.getInstance("EUR"), BigDecimal.ONE));
        to.setTargets(List.of(target));
        return to;
    }

    //@Test
    void selectMethod() throws IOException {
        ScaContextHolder scaDataContext = new ScaContextHolder("qwe", "zxc", "payment");
        StartScaOprTO scaOprTO = AspspMapper.mapStartScaOpr(scaDataContext);
        String login = "anton.brueckner";
        String methodId = "zujftKAGSx0s1awO2vK8jg";
        String response = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/sca/select?login=" + login + "&methodId=" + methodId, client)
                                  .json(scaOprTO)
                                  .asString();
        GlobalScaResponseTO responseTO = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response, GlobalScaResponseTO.class);
        assertNotNull(responseTO.getChallengeData().getAdditionalInformation());
    }

    //@Test
    void validateCode() throws IOException {
        String response = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/validate", client)
                                  .param("authId", "zxc")
                                  .param("code", "123456")
                                  .param("login", "anton.brueckner")
                                  .asString();

        GlobalScaResponseTO responseTO = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response, GlobalScaResponseTO.class);
        boolean isValid = EnumSet.of(ScaStatusTO.FINALISED, ScaStatusTO.UNCONFIRMED).contains(responseTO.getScaStatus());
        boolean mlScaRequired = responseTO.isMultilevelScaRequired();

        CodeValidationResult result = new CodeValidationResult(isValid, mlScaRequired);
        assertNotNull(result);
    }

    //@Test
    void initCancellation() throws IOException {
        PaymentTO pmt = getPayment();
        ConfirmationObject<PaymentTO> object = new ConfirmationObject<>();
        object.setRawBusinessObject(pmt);
        object.setObjType("payment");
        object.setId("qwe");

        int status = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/cancellation/" + "anton.brueckner", client)
                             .param("paymentId", object.getId())
                             .asStatus();
        assertEquals(202, status);
    }
}