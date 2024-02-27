/*
 * Copyright 2018-2024 adorsys GmbH & Co KG
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 *
 * This project is also available under a separate commercial license. You can
 * contact us at sales@adorsys.com.
 */

package de.adorsys.ledgers.app;

import com.google.common.io.Resources;
import de.adorsys.ledgers.keycloak.client.KeycloakClientConfiguration;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.MountableFile;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import static java.lang.String.format;

@SuppressWarnings("rawtypes")
@Import(KeycloakClientConfiguration.class)
public class BaseContainersTest<G, W, T> extends AbstractContainerDatabaseTest<G, W, T> {

    protected static List<GenericContainer> containers;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11")
                                                                       .withDatabaseName("postgres")
                                                                       .withUsername("cms")
                                                                       .withPassword("cms");

    @SneakyThrows
    public static String resource(String path, Map<String, String> params) {
        var body = Resources.asCharSource(Resources.getResource(path), StandardCharsets.UTF_8).read();
        for (var param : params.entrySet()) {
            body = body.replaceAll("%" + param.getKey() + "%", param.getValue());
        }
        return body;
    }

    @AfterEach
    void stopContainers() {
        if (null == containers) {
            return;
        }

        containers.forEach(GenericContainer::stop);
        containers = null;
    }

    @SneakyThrows
    public static String resource(String path) {
        return Resources.asCharSource(Resources.getResource(path), StandardCharsets.UTF_8).read();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        public static Integer randomPort() {
            try (ServerSocket socket = new ServerSocket(0)) {
                return socket.getLocalPort();
            }
        }

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            if (!postgreSQLContainer.isRunning()) {
                postgreSQLContainer.start();
            }

            GenericContainer<?> keycloak = startKeycloak();
            var authUrl = format("http://%s:%d", keycloak.getHost(), keycloak.getMappedPort(8080));

            BaseContainersTest.containers = List.of(keycloak, postgreSQLContainer);
            var port = randomPort();
            RestAssured.baseURI = "http://localhost:" + port;
            RestAssured.config = RestAssured.config.jsonConfig(RestAssured.config.getJsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.show_sql=false",
                    "keycloak.auth-server-url=" + authUrl,
                    format("ledgers.keycloak.redirect-urls=http://%s:%d", keycloak.getHost(), port),
                    "server.port=" + port
            ).applyTo(configurableApplicationContext.getEnvironment());

        }
    }

    @NotNull
    private static GenericContainer<?> startKeycloak() {
        var keycloak = new GenericContainer<>("keycloak/keycloak:22.0")
                               .withEnv("KEYCLOAK_ADMIN", "admin")
                               .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                               .withCommand("start-dev --import-realm")
                               .withCopyToContainer(
                                       MountableFile.forHostPath("../keycloak/keycloak-token-exchange/target/keycloak-token-exchange.jar"),
                                       "/opt/keycloak/providers/keycloak-token-exchange.jar"
                               )
                               .withExposedPorts(8080);
        keycloak.start();
        return keycloak;
    }

    protected void clearData() {
        performQuery(postgreSQLContainer, resource("db/scripts/clear-data.sql"));
    }
}
