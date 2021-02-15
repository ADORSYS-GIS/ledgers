package de.adorsys.keycloak.connector.cms;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.adorsys.keycloak.connector.cms.model.PluginPaymentTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTargetTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTypeTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CmsPaymentDeserializer extends StdDeserializer<PaymentTO> {
    private static final long serialVersionUID = 158931754435907227L;
    private ObjectMapper mapper;

    public CmsPaymentDeserializer(ObjectMapper mapper) {
        super(PaymentTO.class);
        this.mapper = mapper;
    }

    @Override
    public PaymentTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        PaymentTO result = mapper.convertValue(node.get("payment"), PluginPaymentTO.class);

        if (result.getPaymentType() == PaymentTypeTO.BULK) {
            JsonNode paymentsNode = node.get("payment").get("payments");
            List<PaymentTargetTO> targets = new ArrayList<>();

            Iterator<JsonNode> it = paymentsNode.elements();
            while (it.hasNext()) {
                JsonNode target = it.next();
                PaymentTargetTO targetTO = mapper.convertValue(target, PaymentTargetTO.class);
                targets.add(targetTO);
            }
            result.setTargets(targets);
        }

        return result;
    }

}
