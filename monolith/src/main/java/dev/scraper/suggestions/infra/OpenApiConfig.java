package dev.scraper.suggestions.infra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
@OpenAPIDefinition(
        info = @Info(
                title = "Tags API",
                version = "v2",
                description = "This app provides REST APIs for TAGS project"
        ))
@Configuration
public class OpenApiConfig {
    private String authServer;
    private String realm;

    @Autowired
    public OpenApiConfig(@Value("${keycloak.auth-server-url}") String authServer,
                         @Value("${keycloak.realm}") String realm) {
        this.authServer = authServer;
        this.realm = realm;
    }

    @Bean
    public OpenAPI openAPI() {
        var authUrl = String.format("%s/realms/%s/protocol/openid-connect", this.authServer, this.realm);
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("spring_oauth", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Oauth2 flow")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(authUrl + "/auth")
                                                .refreshUrl(authUrl + "/token")
                                                .tokenUrl(authUrl + "/token")
                                                .scopes(new Scopes())
                                        )))
                        .addSecuritySchemes("api_key", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .description("Api Key access")
                                .in(SecurityScheme.In.HEADER)
                                .name("API-KEY")
                        ))
                .security(Arrays.asList(
                        new SecurityRequirement().addList("spring_oauth"),
                        new SecurityRequirement().addList("api_key")));
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.getPaths().values().stream()
                .flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> operation.addParametersItem(new HeaderParameter()
                        .$ref("#/components/parameters/Version")));
    }
}
