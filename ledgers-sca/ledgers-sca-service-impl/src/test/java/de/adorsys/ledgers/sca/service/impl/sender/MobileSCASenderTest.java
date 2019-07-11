package de.adorsys.ledgers.sca.service.impl.sender;

import de.adorsys.ledgers.sca.exception.ScaModuleException;
import org.junit.Test;

public class MobileSCASenderTest {

    @Test(expected = ScaModuleException.class)
    public void send() {
        new MobileSCASender().send("+380933434344", "myAuthCode");
    }
}
