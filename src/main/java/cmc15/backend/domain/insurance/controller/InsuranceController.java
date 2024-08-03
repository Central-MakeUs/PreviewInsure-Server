package cmc15.backend.domain.insurance.controller;

import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.insurance.response.InsuranceResponse;
import cmc15.backend.domain.insurance.service.InsuranceService;
import cmc15.backend.global.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InsuranceController {

    private final InsuranceService insuranceService;

    /**
     * @apiNote 보험 타입 추천 API
     * @param accountId
     * @param insuranceType
     * @return CustomResponseEntity<InsuranceResponse.MapDetail>
     */
    @GetMapping("/recommend")
    public CustomResponseEntity<InsuranceResponse.MapDetail> insuranceRecommend(
            @AuthenticationPrincipal final Long accountId,
            @RequestParam final InsuranceType insuranceType
    ) {
        return CustomResponseEntity.success(insuranceService.insuranceRecommend(accountId, insuranceType));
    }
}
