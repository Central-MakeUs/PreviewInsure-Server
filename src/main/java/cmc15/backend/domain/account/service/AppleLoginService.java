package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.global.Result;
import cmc15.backend.global.exception.CustomException;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cmc15.backend.domain.account.entity.Platform.APPLE;

@Service
@RequiredArgsConstructor
public class AppleLoginService implements OAuth2Service {

    private final static String APPLE_CLIENT_ID = "com.previewinsure";
    private final static String REDIRECT_URI = "https://preview-insure-web-git-dev-sehuns-projects.vercel.app/callback/apple";

    @Override
    public Platform suppots() {
        return APPLE;
    }

    @Override
    public String toOAuthEntityResponse(Platform platform, String code, String appleToken) {
        System.out.println(code);
        try {
            SignedJWT signedJWT = SignedJWT.parse(appleToken);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();
            String email = payload.getClaim("email").toString();
            System.out.println(email);
        } catch (Exception e) {
            throw new CustomException(Result.FAIL);
        }
        return null;
    }
}
