package de.adorsys.ledgers.app.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareResetResource;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareUserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    private static final String API_KEY = "apiKey";
    private static final String API_INFO = "api_info.txt";

    private final FileReader fileReader;
    private final BuildProperties buildProperties;
    private final Environment env;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                       .groupName("001 - LEDGERS API")
                       .select()
                       .apis(resolvePredicates())
                       .paths(PathSelectors.any())
                       .build()
                       .pathMapping("/")
                       .apiInfo(metaData())
                       .securitySchemes(Collections.singletonList(apiKey()))
                       .securityContexts(Collections.singletonList(securityContext()));

    }

    private Predicate<RequestHandler> resolvePredicates() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        return profiles.contains("develop") || profiles.contains("sandbox")
                       ? Predicates.or(RequestHandlerSelectors.withClassAnnotation(MiddlewareUserResource.class),
                                       RequestHandlerSelectors.withClassAnnotation(MiddlewareResetResource.class))
                       : RequestHandlerSelectors.withClassAnnotation(MiddlewareUserResource.class);
    }

    private ApiKey apiKey() {
        return new ApiKey(API_KEY, "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                       .securityReferences(defaultAuth())
                       .forPaths(PathSelectors.regex("/*"))
                       .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(API_KEY, authorizationScopes));
    }

    private ApiInfo metaData() {
        Contact contact = new Contact("Adorsys GmbH", "https://www.adorsys.de", "fpo@adorsys.de");

        return new ApiInfo(
                "Ledgers", fileReader.getStringFromFile(API_INFO),
                buildProperties.getVersion() + " " + buildProperties.get("build.number"),
                "Terms of Service: to be edited...",
                contact,
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}
