package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Platform;

public interface OAuth2Service {

    Platform suppots();

    String toOAuthEntityResponse(final Platform platform, final String code);

}
