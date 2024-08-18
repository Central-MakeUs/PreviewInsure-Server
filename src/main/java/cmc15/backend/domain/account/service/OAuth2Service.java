package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.response.AccountResponse;

public interface OAuth2Service {

    Platform suppots();

    AccountResponse.OAuthConnection toSocialLoginAccount(final Platform platform, final String code);

}
