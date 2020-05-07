package io.github.jhipster.sample.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata;
import io.micronaut.security.oauth2.configuration.endpoints.EndSessionConfiguration;
import io.micronaut.security.oauth2.endpoint.endsession.request.EndSessionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.net.URI;

@Named("oidc")
public class KeycloakEndSessionEndpoint implements EndSessionEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakEndSessionEndpoint.class);

    public static final String PARAM_REDIRECT_URI = "redirect_uri";
    private final OpenIdProviderMetadata openIdProviderMetadata;
    private final EndSessionConfiguration endSessionConfiguration;
    private final HttpHostResolver httpHostResolver;

    public KeycloakEndSessionEndpoint(@Named("oidc") OpenIdProviderMetadata openIdProviderMetadata,
                                      EndSessionConfiguration endSessionConfiguration,
                                      HttpHostResolver httpHostResolver) {
        this.openIdProviderMetadata = openIdProviderMetadata;
        this.endSessionConfiguration = endSessionConfiguration;
        this.httpHostResolver = httpHostResolver;
    }

    @Nullable
    @Override
    public String getUrl(HttpRequest originating, Authentication authentication) {
        if (openIdProviderMetadata.getEndSessionEndpoint() == null) {
            return null;
        }
        String url = UriBuilder.of(URI.create(openIdProviderMetadata.getEndSessionEndpoint()))
            .queryParam(PARAM_REDIRECT_URI, httpHostResolver.resolve(originating) + endSessionConfiguration.getRedirectUri())
            .build()
            .toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Endsession url: {}", url);
        }
        return url;
    }
}