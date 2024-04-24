package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BaseStage<SELF extends Stage<?>> extends Stage<SELF> {

    @ScenarioState
    protected ExtractableResponse<Response> response;

    public <T> SELF path(String path, Consumer<T> pathConsumer) {
        pathConsumer.accept(this.response.path(path));
        return self();
    }

    public SELF pathStr(String path, Consumer<String> pathConsumer) {
        pathConsumer.accept(this.response.path(path));
        return self();
    }

    public <T> SELF body(Consumer<T> bodyConsumer) {
        bodyConsumer.accept(this.response.body().path(""));
        return self();
    }

    @NotNull
    protected static String getAccessToken(ExtractableResponse<Response> resp) {
        return "Bearer " + resp.path("access_token");
    }

    @NotNull
    protected static String getBearerToken(ExtractableResponse<Response> resp) {
        return "Bearer " + resp.path("bearerToken.access_token");
    }
}
