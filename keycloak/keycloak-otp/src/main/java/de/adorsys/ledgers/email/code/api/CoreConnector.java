package de.adorsys.ledgers.email.code.api;

import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import de.adorsys.ledgers.email.code.domain.ScaMethod;

import java.util.List;

public interface CoreConnector {
    List<ScaMethod> getMethods();

    void initObj(ScaContextHolder holder, Object object);

    void selectMethod(ScaContextHolder holder, String methodId);

    boolean validateCode(ScaContextHolder holder, String code);

    void execute(ScaContextHolder holder);
}
