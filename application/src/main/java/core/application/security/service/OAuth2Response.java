package core.application.security.service;

public interface OAuth2Response {
    // Resource Server
    String getProvider();

    // Resource Server에서 제공해주는 id
    String getProviderId();

    String getEmail();

    String getName();

    String getAlias();

    String toString();
}
