package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
import cmc15.backend.domain.account.response.FavoriteInsuranceResponse;
import cmc15.backend.domain.account.service.FavoriteInsuranceService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteInsuranceController {

    private final FavoriteInsuranceService favoriteInsuranceService;

    /**
     * @param accountId
     * @param request
     * @apiNote 관심보험 등록 API
     */
    @PostMapping("/account/favorite")
    public CustomResponseEntity<Void> addFavoriteInsurance(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final FavoriteInsuranceRequest.Add request
    ) {
        return CustomResponseEntity.success(favoriteInsuranceService.addFavoriteInsurance(accountId, request));
    }

    /**
     * @param accountId
     * @apiNote 내 관심보험 전체 조회 API
     */
    @GetMapping("/account/favorite")
    public CustomResponseEntity<List<FavoriteInsuranceResponse.Detail>> readFavoriteInsurances(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(favoriteInsuranceService.readFavoriteInsurances(accountId));
    }

    /**
     * @param accountId
     * @param request
     * @apiNote 내 관심 보험 취소 API
     */
    @DeleteMapping("/account/favorite")
    public CustomResponseEntity<Void> deleteFavoriteInsurance(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final FavoriteInsuranceRequest.Delete request
    ) {
        return CustomResponseEntity.success(favoriteInsuranceService.deleteFavoriteInsurance(accountId, request));
    }
}
