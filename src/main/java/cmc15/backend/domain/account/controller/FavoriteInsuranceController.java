package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
import cmc15.backend.domain.account.service.FavoriteInsuranceService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteInsuranceController {

    private final FavoriteInsuranceService favoriteInsuranceService;

    @PostMapping("/account/favorite")
    public CustomResponseEntity<Void> addFavoriteInsurance(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final FavoriteInsuranceRequest.Add request
    ) {
        return CustomResponseEntity.success(favoriteInsuranceService.addFavoriteInsurance(accountId, request));
    }
}
