package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BaseStage<SELF extends Stage<?>> extends Stage<SELF> {

    @ScenarioState
    protected ExtractableResponse<Response> response;

    public SELF response(Consumer<ExtractableResponse<Response>> responseConsumer) {
        responseConsumer.accept(this.response);
        return self();
    }

    public <T> SELF body(Consumer<T> bodyConsumer) {
        bodyConsumer.accept(this.response.body().path(""));
        return self();
    }

    public <T> SELF path(String path, Consumer<T> pathConsumer) {
        pathConsumer.accept(this.response.path(path));
        return self();
    }

    public SELF pathStr(String path, Consumer<String> pathConsumer) {
        pathConsumer.accept(this.response.path(path));
        return self();
    }

    public SELF pathInt(String path, Consumer<Integer> pathConsumer) {
        pathConsumer.accept(this.response.path(path));
        return self();
    }

    public SELF pathListNum(String path, Consumer<List<String>> pathConsumer) {
        path(
            path,
            (List<BigDecimal> it) -> pathConsumer.accept(it.stream().map(BigDecimal::toPlainString).collect(Collectors.toList()))
        );
        return self();
    }

    public SELF pathListStr(String path, Consumer<List<String>> pathConsumer) {
        path(path, pathConsumer);
        return self();
    }
}
